package com.telegramskbot.service;

import com.telegramskbot.model.UpdateMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramBotService {

    private final RestTemplate restTemplate;
    private final String botToken;

    public TelegramBotService(RestTemplate restTemplate, @Value("${telegram.bot.token}") String botToken) {
        this.restTemplate = restTemplate;
        this.botToken = botToken;
    }

    /**
     * Sends a message to Telegram using POST with JSON body to avoid URL-encoding issues.
     * Handles parse_mode if set in the UpdateMessage.
     */
    public void sendMessage(UpdateMessage telegramMessage) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        Map<String, Object> body = new HashMap<>();
        body.put("chat_id", telegramMessage.getChatId());
        body.put("text", telegramMessage.getMessage());
        if (telegramMessage.getParseMode() != null) {
            body.put("parse_mode", telegramMessage.getParseMode());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            System.out.println("Sending message to chatId: " + telegramMessage.getChatId());
            System.out.println("Message: " + telegramMessage.getMessage());
            System.out.println("Request body: " + body);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Message sent successfully: " + response.getBody());
        } catch (RestClientException e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to allow higher-level handling
        }
    }

    /**
     * Overloaded method to send a simple message with chatId and text.
     * Returns the response or error message.
     */
    public String sendMessage(String chatId, String message) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        Map<String, Object> body = new HashMap<>();
        body.put("chat_id", chatId);
        body.put("text", message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            System.out.println("Sending message to chatId: " + chatId);
            System.out.println("Message: " + message);
            System.out.println("Request body: " + body);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Message sent successfully: " + response.getBody());
            return response.getBody();
        } catch (RestClientException e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
            return "Error sending message: " + e.getMessage();
        }
    }

    /**
     * Gets updates from Telegram server using long polling.
     */
    public String getUpdates() {
        String url = "https://api.telegram.org/bot" + botToken + "/getUpdates";

        try {
            System.out.println("Getting updates from Telegram...");
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            System.out.println("Updates received: " + response.getBody());
            return response.getBody();
        } catch (RestClientException e) {
            System.err.println("Error getting updates: " + e.getMessage());
            e.printStackTrace();
            return "Error getting updates: " + e.getMessage();
        }
    }
}