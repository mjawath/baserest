package com.techstart.base.rest.controller.exceptions;

public class ServerError extends RuntimeException{
    public ServerError(String message) {
        super(message);
    }
}
