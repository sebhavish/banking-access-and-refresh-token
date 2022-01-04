package com.cg.banking.beans;


public class ErrorResponse {
    private String message;


    public ErrorResponse(Exception exception) {
        this.message = exception.getMessage();
    }

    public String getMessage() {
        return message;
    }
}
