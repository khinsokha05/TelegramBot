package com.telegramskbot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.telegramskbot.model.UpdateMessage;
import com.telegramskbot.service.TelegramBotService;
@SpringBootApplication
@EnableScheduling
public class TbotApplication {

        public static void main(String[] args) throws TelegramApiException {
            SpringApplication.run(TbotApplication.class, args);
            System.out.println("Hello world!");
    
            try{
                UpdateMessage send = new UpdateMessage();
                send.setMessage("Hello from Spring Boot!0000000000");
                send.setChatId("2126941820");
                TelegramBotService.sendMessage(send);

              

            }catch(Exception e){
                System.out.println(e);
            }
 


    }

}