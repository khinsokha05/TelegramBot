package com.telegramskbot.helper;

import com.telegramskbot.model.UpdateMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class CommandHandler {

    // Thread-safe map of commands to their handlers
    private final Map<String, Function<String, String>> commandHandlers = new HashMap<>();

    public CommandHandler() {
        // Register default commands
        commandHandlers.put("/start", this::handleStartCommand);
        commandHandlers.put("/help", this::handleHelpCommand);
        commandHandlers.put("/ping", this::handlePingCommand);
    }

    /**
     * Process an incoming message and return a response if it's a recognized command.
     * @param message The UpdateMessage containing the text and chat ID
     * @return UpdateMessage with the response, or null if not a command or no response
     */
    public UpdateMessage processCommand(UpdateMessage message) {
        if (message == null || message.getMessage() == null || !message.getMessage().startsWith("/")) {
            return null;
        }

        String text = message.getMessage().trim();
        String[] parts = text.split("\\s+", 2);
        String command = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        Function<String, String> handler = commandHandlers.get(command);
        if (handler == null) {
            return createResponse(message.getChatId(), "Unknown command. Type /help for available commands.");
        }

        String responseText = handler.apply(args);
        if (responseText == null || responseText.isBlank()) {
            return null;
        }

        return createResponse(message.getChatId(), responseText);
    }

    /**
     * Create a response message with the given chat ID and text.
     */
    private UpdateMessage createResponse(String chatId, String text) {
        UpdateMessage response = new UpdateMessage();
        response.setChatId(chatId);
        response.setMessage(text);
        // response.setSetParseMode("Markdown");
        return response;
    }

    // Command handler methods
    private String handleStartCommand(String args) {
        return "Welcome to the bot! Use /help to see available commands.";
    }

    private String handleHelpCommand(String args) {
        StringBuilder helpText = new StringBuilder("Available commands:\n");
        helpText.append("/start - Start the bot\n");
        helpText.append("/help - Show this help message\n");
        helpText.append("/ping - Check if the bot is alive");
        return helpText.toString();
    }

    private String handlePingCommand(String args) {
        return "Pong! Bot is alive.";
    }
}