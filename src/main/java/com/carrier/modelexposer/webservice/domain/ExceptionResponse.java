package com.carrier.modelexposer.webservice.domain;

public class ExceptionResponse extends Response {
    private String message;

    public ExceptionResponse() {
    }

    public ExceptionResponse(Exception e) {
        this.message = e.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
