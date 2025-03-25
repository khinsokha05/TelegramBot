package com.telegramskbot.RestConnector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class Rest {
    private RestTemplate restTemplate = new RestTemplate();
    
    @Autowired
    public void TelegramMessageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String fetchData() {
        return restTemplate.getForObject("https://api.telegram.org/bot", String.class);
    }
}

