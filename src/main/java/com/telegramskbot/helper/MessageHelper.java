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

    public MessageHelper(@Value("${telegram.chat.id}") String defaultChatId) {
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

                // Prefer "message", fall back to "edited_message" or "channel_post" if needed
                JsonObject message = getAsObject(update, "message");
                if (message == null) message = getAsObject(update, "edited_message");
                if (message == null) message = getAsObject(update, "channel_post");
                if (message == null) {
                    processedUpdates.add(updateId); // mark anyway to avoid re-processing
                    continue;
                }

                // Only handle text messages for now
                String text = getAsString(message);
                if (text == null || text.isBlank()) {
                    processedUpdates.add(updateId);
                    continue;
                }

                // Determine where to reply:
                // 1) If you configured a default chat id, use it
                // 2) Otherwise reply back to the chat the update came from
                String chatIdToUse = this.defaultChatId;
                if (chatIdToUse == null || chatIdToUse.isBlank()) {
                    JsonObject chatObj = getAsObject(message, "chat");
                    Long chatIdFromUpdate = chatObj != null ? getAsLong(chatObj, "id") : null;
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
                // out.setSetParseMode("Markdown"); // enable if you need formatting

                messagesToSend.add(out);
                processedUpdates.add(updateId);

                // Simple memory cap (avoid unbounded growth)
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

    private static String getAsString(JsonObject obj) {
        if (obj == null || !obj.has("text")) return null;
        JsonElement e = obj.get("text");
        return (e != null && !e.isJsonNull()) ? e.getAsString() : null;
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
