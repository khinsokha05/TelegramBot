package com.telegramskbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.telegramskbot.model.UpdateMessage;
import com.telegramskbot.service.TelegramBotService;

public class TelegramBotController {

    @Autowired
    public TelegramBotController(TelegramBotService telegramBotService) {
    }
    
    @PostMapping("/sendMessage")
    public String sendMessage(@RequestParam String chatId, @RequestParam String message) {
        // Create a message object to send to the Telegram service
        UpdateMessage telegramMessage = new UpdateMessage();
        telegramMessage.setChatId(chatId);
        telegramMessage.setMessage(message);

        // Call the service to send the message to Telegram
        TelegramBotService.sendMessage(telegramMessage);

        // Return confirmation message to the user
        return "Message sent successfully!";
    }
}
