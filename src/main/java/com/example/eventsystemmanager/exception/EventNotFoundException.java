package com.example.eventsystemmanager.exception;

public class EventNotFoundException extends Exception {

    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
