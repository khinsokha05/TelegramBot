package com.telegramskbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.telegramskbot.model.UpdateMessage;

@Service
public class TelegramBotService {

    private final static String BOT_API_URL = "https://api.telegram.org/bot";
    private final static String BOT_TOKEN = "6195519007:AAEcb8KEugXODgfHz_-vkgS2rXrQ7-ow_pc";
    private static RestTemplate restTemplate = new RestTemplate();

    // Constructor-based dependency injection
    @Autowired
    public TelegramBotService(RestTemplate restTemplate) {
        TelegramBotService.restTemplate = restTemplate;
    }

    public static void sendMessage(UpdateMessage telegramMessage) {

        String url = UriComponentsBuilder.fromHttpUrl(BOT_API_URL + BOT_TOKEN + "/sendMessage")
                .queryParam("chat_id", telegramMessage.getChatId())
                .queryParam("text", telegramMessage.getMessage())
                .toUriString();

        System.out.println("url : "  + url);
        restTemplate.getForObject(url, String.class);
    }
    

    public String sendMessage(String chatId, String message) {
        try {
            System.out.println("Sending message to chatId: " + chatId);
            System.out.println("Message: " + message);
            
            String url = UriComponentsBuilder.fromHttpUrl(BOT_API_URL + BOT_TOKEN + "/sendMessage")
                                            .queryParam("chat_id", chatId)
                                            .queryParam("text", message)
                                            .toUriString();

            System.out.println("Request URL: " + url);

            String response = restTemplate.getForObject(url, String.class);

            return response; 
        } catch (Exception e) {
            // If an exception occurs, return an error message
            e.printStackTrace();
            return "Error sending message: " + e.getMessage();
        }
    }

      /**
     * Gets updates from Telegram server using long polling
     */
    public String getUpdates() {
        String url = UriComponentsBuilder.fromHttpUrl(BOT_API_URL + BOT_TOKEN + "/getUpdates")
        .toUriString();               
       
        try {
            System.out.println("Getting updates from Telegram...");
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Updates received: " + response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error getting updates: " + e.getMessage();
        }
    }
    
}
