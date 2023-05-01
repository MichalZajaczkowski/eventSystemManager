package com.example.eventsystemmanager.exception;

public class DuplicateEntityException extends Exception{
    public DuplicateEntityException(String addressId) {
        super("Could not find address with id: " + addressId);
    }
}
