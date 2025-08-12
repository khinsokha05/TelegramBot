package com.telegramskbot.helper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.telegramskbot.model.UpdateMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageHelper {

    // Thread-safe set; avoids races between schedulers
    private final Set<Long> processedUpdates = ConcurrentHashMap.newKeySet();

    // Default/fallback chat id from application properties
    private final String defaultChatId;

    // Reuse one Gson instance
    private final Gson gson = new Gson();

    // Inject CommandHandler
    private final CommandHandler commandHandler;

    public MessageHelper(@Value("${telegram.chat.id}") String defaultChatId, CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        this.defaultChatId = defaultChatId;
    }

    /**
     * Parse Telegram getUpdates JSON and produce messages to send.
     * Safe against missing fields and non-text updates.
     */
    public List<UpdateMessage> processUpdates(String jsonData) {
        List<UpdateMessage> messagesToSend = new ArrayList<>();

        try {
            JsonObject root = gson.fromJson(jsonData, JsonObject.class);
            if (root == null) return messagesToSend;

            JsonArray results = root.getAsJsonArray("result");
            if (results == null) return messagesToSend;

            for (JsonElement el : results) {
                if (!el.isJsonObject()) continue;
                JsonObject update = el.getAsJsonObject();

                Long updateId = getAsLong(update, "update_id");
                if (updateId == null || processedUpdates.contains(updateId)) continue;

                // Prefer "message", fall back to "edited_message" or "channel_post"
                JsonObject message = getAsObject(update, "message");
                if (message == null) message = getAsObject(update, "edited_message");
                if (message == null) message = getAsObject(update, "channel_post");
                if (message == null) {
                    processedUpdates.add(updateId);
                    continue;
                }

                // Only handle text messages for now
                String text = getAsString(message, "text");
                if (text == null || text.isBlank()) {
                    processedUpdates.add(updateId);
                    continue;
                }
                
                // Skip updates newer than 1 minute
                // Use message date (or edit_date for edited messages)
                Long msgUnix = getAsLong(message, "date");
                if (msgUnix == null) msgUnix = getAsLong(message, "edit_date"); // optional

                long nowSec = System.currentTimeMillis() / 1000L;
                long ageSec = (msgUnix != null) ? (nowSec - msgUnix) : Long.MAX_VALUE;

                if (ageSec > 60) {
                    processedUpdates.add(updateId);
                    continue;
                }

                // Get chat object
                JsonObject chatObj = getAsObject(message, "chat");
                String chatType = chatObj != null ? getAsString(chatObj, "type") : null;
                Long chatIdFromUpdate = chatObj != null ? getAsLong(chatObj, "id") : null;

                // Decide the chat id to send to
                String chatIdToUse = this.defaultChatId;

                // If the message came from a group, use that group's id
                if ("group".equalsIgnoreCase(chatType) || "supergroup".equalsIgnoreCase(chatType)) {
                    if (chatIdFromUpdate != null) {
                        chatIdToUse = String.valueOf(chatIdFromUpdate);
                    }
                } else if (chatIdToUse == null || chatIdToUse.isBlank()) {
                    if (chatIdFromUpdate != null) {
                        chatIdToUse = String.valueOf(chatIdFromUpdate);
                    }
                }

                if (chatIdToUse == null || chatIdToUse.isBlank()) {
                    processedUpdates.add(updateId);
                    continue; // nowhere to send
                }

   

                UpdateMessage out = new UpdateMessage();
                out.setMessage(text);
                out.setChatId(chatIdToUse);
                out.setParseMode("Markdown");

                UpdateMessage commandResponse = commandHandler.processCommand(out);
                if (commandResponse != null) {
                    messagesToSend.add(commandResponse);
                } else {
                    // Handle non-command messages here if needed
                    // For now, we'll just add the original message
                    messagesToSend.add(out);
                }

                processedUpdates.add(updateId);

                // Simple memory cap
                if (processedUpdates.size() > 10_000) {
                    processedUpdates.clear();
                }
            }

        } catch (Exception e) {
            System.out.println("Error processing updates: " + e.getMessage());
        }

        return messagesToSend;
    }


    // --------- small JSON helpers to avoid NPEs ----------

    private static JsonObject getAsObject(JsonObject obj, String key) {
        if (obj == null || !obj.has(key)) return null;
        JsonElement e = obj.get(key);
        return e != null && e.isJsonObject() ? e.getAsJsonObject() : null;
    }

    private static String getAsString(JsonObject obj, String key) {
        if (obj == null || key == null) {
            return null;
        }
        if (!obj.has(key)) {
            return null;
        }
        JsonElement element = obj.get(key);
        if (element == null || element.isJsonNull()) {
            return null;
        }
        try {
            return element.getAsString();
        } catch (UnsupportedOperationException | ClassCastException e) {
            // If the field exists but isn't a string (e.g., number), safely return null
            return null;
        }
    }


    private static Long getAsLong(JsonObject obj, String key) {
        if (obj == null || !obj.has(key)) return null;
        JsonElement e = obj.get(key);
        try {
            return (e != null && !e.isJsonNull()) ? e.getAsLong() : null;
        } catch (Exception ignored) {
            return null;
        }
    }
}
