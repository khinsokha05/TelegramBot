package com.telegramskbot.model;

public class UpdateMessage {
    private String chatId;
    private String message;
    private String parseMode;
    
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
    public String getParseMode() {
        return parseMode;
    }
    public void setParseMode(String parseMode) {
        this.parseMode = parseMode;
    }
    
}
