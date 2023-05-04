package com.example.eventsystemmanager.exception;

public class EventSaveException extends RuntimeException {
    public EventSaveException(String message) {
        super(message);
    }

    public EventSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}

