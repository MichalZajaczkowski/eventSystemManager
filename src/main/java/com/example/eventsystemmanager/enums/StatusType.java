package com.example.eventsystemmanager.enums;

public enum StatusType {
    AKTYWNY("AKTYWNY", 0),
    NIEAKTYWNY("NIEAKTYWNY", 1),
    DO_AKTYWACJI("DO AKTYWACJI", 2),
    ZABLOKOWANY("ZABLOKOWANY", 3);

    private final String name;
    private final Integer value;

    StatusType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public static StatusType fromValue(Integer value) {
        for (StatusType status : StatusType.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Nieprawidłowa wartość statusu: " + value);
    }

    public static StatusType fromName(String name) {
        for (StatusType status : StatusType.values()) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Nieprawidłowa nazwa statusu: " + name);
    }
}

