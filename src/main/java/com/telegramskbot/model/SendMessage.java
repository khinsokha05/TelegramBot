package com.telegramskbot.model;

public class SendMessage {
    private String chatId;
    private String message;
    
    public String getChatId() {
        return chatId;
    }
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    

}
