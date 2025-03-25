package com.telegramskbot.model;

public class UpdateMessage {
    private String chatId;
    private String message;
    private String setParseMode;
    
    public String getSetParseMode() {
        return setParseMode;
    }
    public void setSetParseMode(String setParseMode) {
        this.setParseMode = setParseMode;
    }
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
