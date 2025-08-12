package com.telegramskbot.controller;

import com.telegramskbot.model.UpdateMessage;
import com.telegramskbot.service.TelegramBotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelegramBotController {

    private final TelegramBotService telegramBotService;

    public TelegramBotController(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@RequestParam String chatId, @RequestParam String message) {
        try {
            // Create a message object to send to the Telegram service
            UpdateMessage telegramMessage = new UpdateMessage();
            telegramMessage.setChatId(chatId);
            telegramMessage.setMessage(message);
            telegramMessage.setParseMode("Markdown"); // Set parse mode for consistency with AlertSchedulerService

            // Call the service to send the message to Telegram
            telegramBotService.sendMessage(telegramMessage);

            // Return confirmation message to the user
            return ResponseEntity.ok("Message sent successfully!");
        } catch (Exception e) {
            // Return error response if sending fails
            return ResponseEntity.status(500).body("Error sending message: " + e.getMessage());
        }
    }
}