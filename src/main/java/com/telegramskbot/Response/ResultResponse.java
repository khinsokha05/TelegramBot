package com.telegramskbot.Response;

public class ResultResponse<T> {
    private ResultMessage resultMessage;
    private T object;

    // Constructor, Getters, and Setters
    public ResultResponse(ResultMessage resultMessage, T object) {
        this.resultMessage = resultMessage;
        this.object = object;
    }

    // Getters and Setters
    public ResultMessage getResultMessage() { return resultMessage; }
    public T getObject() { return object; }
}