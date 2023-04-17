package com.example.eventsystemmanager.user.userStatus;

public enum UserStatus {
    AKTYWNY("AKTYWNY", 0),
    NIEAKTYWNY("NIEAKTYWNY", 1),
    DO_AKTYWACJI("DO AKTYWACJI", 2),
    ZABLOKOWANY("ZABLOKOWANY", 3);

    private final String name;
    private final Integer value;

    UserStatus(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public static UserStatus fromValue(Integer value) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Nieprawidłowa wartość statusu: " + value);
    }

    public static UserStatus fromName(String name) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getName().equals(name)) {
                return status; 
            }
        }
        throw new IllegalArgumentException("Nieprawidłowa nazwa statusu: " + name);
    }
}

