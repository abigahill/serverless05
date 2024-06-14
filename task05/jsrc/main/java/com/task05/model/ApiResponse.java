package com.task05.model;

public class ApiResponse {
    private int statusCode;
    private Object event;

    public ApiResponse() {
    }

    public ApiResponse(int statusCode, Object event) {
        this.statusCode = statusCode;
        this.event = event;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
        this.event = event;
    }
}