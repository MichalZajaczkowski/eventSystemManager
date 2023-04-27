package com.example.eventsystemmanager.address.addressType;

public enum AddressType {

    USER_ADDRESS("User Address", 0),
    PLACE_ADDRESS("Place Address", 1);
    private final String name;
    private final Integer value;

    AddressType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public static AddressType fromValue(Integer value) {
        for (AddressType type : AddressType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Nieprawidłowa wartość typu: " + value);
    }

    public static AddressType fromName(Integer name) {
        for (AddressType type : AddressType.values()) {
            if (type.getValue().equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Nieprawidłowa wartość typu: " + name);
    }


}
