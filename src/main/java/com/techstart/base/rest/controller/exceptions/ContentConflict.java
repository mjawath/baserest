package com.techstart.base.rest.controller.exceptions;

public class ContentConflict extends RuntimeException{
    public ContentConflict(String message) {
        super(message);
    }
}
