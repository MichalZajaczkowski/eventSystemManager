package com.example.eventsystemmanager.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(Long addressId) {
        super("Could not find address with id: " + addressId);
    }
}
