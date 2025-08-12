// package com.telegramskbot.service;

// import com.telegramskbot.helper.MessageHelper;
// import com.telegramskbot.model.UpdateMessage;
// import com.telegramskbot.pattern.StringUtil;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Service;

// import java.util.List;

// import javax.annotation.PreDestroy;

// @Service
// public class AlertSchedulerService {

//     @Value("${telegram.bot.token}")
//     private String botToken;

//     @Value("${telegram.chat.id}")
//     private String chatId;

//     private volatile boolean sendingEnabled = true;

//     private final TelegramBotService telegramBotService;
//     private final MessageHelper messageHelper;

//     // Constructor injection for required dependencies
//     public AlertSchedulerService(TelegramBotService telegramBotService, MessageHelper messageHelper) {
//         this.telegramBotService = telegramBotService;
//         this.messageHelper = messageHelper;
//     }

//     // Send a message to Telegram
//     public void sendMessage(String message) {
//         try {
//             UpdateMessage send = new UpdateMessage();
//             send.setMessage(message);
//             send.setChatId(chatId);
//             send.setSetParseMode("Markdown");
//             telegramBotService.sendMessage(send);
//             System.out.println("Message sent successfully: " + message);
//         } catch (Exception e) {
//             System.out.println("Error sending message: " + e.getMessage());
//         }
//     }

//     // Scheduled every 10 seconds
//     @Scheduled(fixedRate = 100000)
//     public void sendPeriodicAlert() {
//         if (!sendingEnabled) return;

//         String randomNum = StringUtil.generateRandomSixDigitNumber();
//         System.out.println("Random 6-digit number: " + randomNum);
//         sendMessage(randomNum);
//     }

//     // Poll Telegram for updates every 1 second
//     @Scheduled(fixedDelay = 1000)
//     public void pollForUpdates() {
//         if (!sendingEnabled) return;

//         try {
//             String updatesJson = telegramBotService.getUpdates();
//             System.out.println("Updates received: " + updatesJson);

//             List<UpdateMessage> messagesToSend = messageHelper.processUpdates(updatesJson);

//             for (UpdateMessage message : messagesToSend) {
//                 telegramBotService.sendMessage(message);
//             }
//         } catch (Exception e) {
//             System.out.println("Error polling updates: " + e.getMessage());
//         }
//     }

//     @PreDestroy
//     public void onShutdown() {
//         sendingEnabled = false;
//         System.out.println("Application is shutting down. Cleanup done.");
//     }
// }


package com.telegramskbot.service;

import com.telegramskbot.helper.MessageHelper;
import com.telegramskbot.model.UpdateMessage;
import com.telegramskbot.pattern.StringUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.PreDestroy;

@Service
public class AlertSchedulerService {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;

    private volatile boolean sendingEnabled = true;

    private final TelegramBotService telegramBotService;
    private final MessageHelper messageHelper;

    // Constructor injection for required dependencies
    public AlertSchedulerService(TelegramBotService telegramBotService, MessageHelper messageHelper) {
        this.telegramBotService = telegramBotService;
        this.messageHelper = messageHelper;
    }

    // Send a message to Telegram
    public void sendMessage(String message) {
        try {
            UpdateMessage send = new UpdateMessage();
            send.setMessage(message);
            send.setChatId(chatId);
            send.setParseMode("Markdown"); // Fixed typo: assuming it's setParseMode, not setSetParseMode
            telegramBotService.sendMessage(send);
            System.out.println("Message sent successfully: " + message);
        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    // Scheduled every 100 seconds (fixed comment to match fixedRate value)
    @Scheduled(fixedRate = 100000)
    public void sendPeriodicAlert() {
        if (!sendingEnabled) return;

        String randomNum = StringUtil.generateRandomSixDigitNumber();
        System.out.println("Random 6-digit number: " + randomNum);
        sendMessage(randomNum);
    }

    // Poll Telegram for updates every 1 second
    @Scheduled(fixedDelay = 1000)
    public void pollForUpdates() {
        if (!sendingEnabled) return;

        try {
            String updatesJson = telegramBotService.getUpdates();
            System.out.println("Updates received: " + updatesJson);

            List<UpdateMessage> messagesToSend = messageHelper.processUpdates(updatesJson);

            for (UpdateMessage message : messagesToSend) {
                telegramBotService.sendMessage(message);
            }
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