package com.telegramskbot.Response;

// ResultMessage.java
public class ResultMessage {
    private int status;
    private int code;
    private String message;

    // Constructor, Getters, and Setters
    public ResultMessage(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    // Getters and Setters (required for JSON serialization)
    public int getStatus() { return status; }
    public int getCode() { return code; }
    public String getMessage() { return message; }
}