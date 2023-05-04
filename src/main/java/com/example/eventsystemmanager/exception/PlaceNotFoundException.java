package com.example.eventsystemmanager.exception;

public class PlaceNotFoundException extends RuntimeException{
    public PlaceNotFoundException(Long placeID) {
        super("Could not find address with id: " + placeID);
    }
}
