package com.telegramskbot.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.telegramskbot.model.UpdateMessage;
import com.telegramskbot.pattern.StringUtil;

import javax.annotation.PreDestroy;
@Service
public class AlertSchedulerService {
    @Value("${telegram.bot.token}")
    private String botToken;
    
    @Value("${telegram.chat.id}")
    private String chatId;
    
    private volatile boolean sendingEnabled = true;
    
    private final TelegramBotService telegramBotService;
    
    @Autowired
    public AlertSchedulerService(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }
    
    public void sendMessage(String message) {
        try {
            UpdateMessage send = new UpdateMessage();
            send.setMessage(message);
            send.setChatId(chatId);
            send.setSetParseMode("Markdown");
            telegramBotService.sendMessage(send);
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
        System.out.println("Message sent successfully: " + message);
    }
    
    @Scheduled(fixedRate = 10000)
    public void sendPeriodicAlert() {
        if (!sendingEnabled) return;
        
        String randomNum = StringUtil.generateRandomSixDigitNumber();
        System.out.println("Random 6-digit number: " + randomNum);
        sendMessage(randomNum);
    }
    
    @Scheduled(fixedDelay = 1000)
    public void pollForUpdates() {
        if (!sendingEnabled) return;
        
        try {
            String updatesJson = telegramBotService.getUpdates();
            System.out.println("Updates received: " + updatesJson);
            UpdateMessage send = new UpdateMessage();
            send.setMessage(updatesJson);
            send.setChatId(chatId);
            TelegramBotService.sendMessage(send);
        } catch (Exception e) {
            System.out.println("Error polling updates: " + e.getMessage());
        }
    }
    
    @PreDestroy
    public void onShutdown() {
        sendingEnabled = false;
        System.out.println("Application is shutting down. Cleanup done.");
    }
}