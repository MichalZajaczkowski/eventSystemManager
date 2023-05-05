package com.example.eventsystemmanager.exception;

public class PlaceNotFoundException extends RuntimeException {
    public PlaceNotFoundException(String placeID) {
        super("Could not find place with id: " + placeID);
    }
}
