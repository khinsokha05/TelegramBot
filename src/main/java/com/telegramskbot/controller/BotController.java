// package com.telegramskbot.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.telegram.telegrambots.meta.TelegramBotsApi;
// import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
// import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

// import com.telegramskbot.service.BotService;

// @RestController
// public class BotController {
//     private final BotService botService;

//     @Autowired
//     public BotController(BotService botService) {
//         this.botService = botService;
//         initializeBot();
//     }

//     private void initializeBot() {
//         try {
//             TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//             botsApi.registerBot(botService);
//         } catch (TelegramApiException e) {
//             e.printStackTrace();
//         }
//     }

//     @GetMapping("/health")
//     public String healthCheck() {
//         return "Bot is running";
//     }
// }