package com.telegramskbot;

import com.telegramskbot.model.UpdateMessage;
import com.telegramskbot.service.TelegramBotService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TbotApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(TbotApplication.class, args);
        System.out.println("Hello world!");

        try {
            // Get the Spring bean (non-static)
            TelegramBotService bot = ctx.getBean(TelegramBotService.class);

            // Read chat id from properties (don’t hardcode)
            Environment env = ctx.getEnvironment();
            String chatId = env.getProperty("telegram.chat.id", "2126941820"); // fallback if missing

            UpdateMessage send = new UpdateMessage();
            send.setMessage("Hello");
            send.setChatId(chatId);
            // send.setSetParseMode("Markdown");

            bot.sendMessage(send);
        } catch (Exception e) {
            System.out.println("Failed to send message: " + e.getMessage());
        }
    }
}
