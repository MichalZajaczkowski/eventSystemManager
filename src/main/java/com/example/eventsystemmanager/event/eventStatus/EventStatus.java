package com.example.eventsystemmanager.event.eventStatus;

public enum EventStatus {
    UPCOMING(0, "upcoming", "Event is upcoming"),
    ACTIVE(1, "active", "Event is active"),
    POSTPONED(2, "postponed", "Event has been postponed"),
    CANCELLED(3, "cancelled", "Event has been cancelled"),
    FINISHED(4, "finished", "Event has finished");
    private final Integer value;
    private final String status;
    private final String description;

    EventStatus(Integer value, String status, String description) {
        this.value = value;
        this.status = status;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public static EventStatus fromString(String status) {
        for (EventStatus eventStatus : EventStatus.values()) {
            if (eventStatus.status.equalsIgnoreCase(status)) {
                return eventStatus;
            }
        }
        throw new IllegalArgumentException("No constant with status " + status + " found");
    }
}