package com.example.eventsystemmanager.category;

public enum Category {
    MUSIC(1, "music", "Concerts, festivals and music events"),
    SPORT(2, "sport", "Sporting events and competitions"),
    ART(3,"art", "Art exhibitions and installations"),
    FOOD(4, "food","Food festivals and events"),
    EDUCATION(5,"education", "Educational workshops and conferences"),
    TECH(6, "tech","Technology and innovation events"),
    BUSINESS(7,"busines", "Business and networking events"),
    CHARITY(8,"charity", "Charity and fundraising events"),
    FASHION(9, "fashion","Fashion shows and events"),
    TRAVEL(10,"travel", "Travel and tourism events"),
    CULTURE(11, "culture","Cultural festivals and events"),
    HEALTH(12,"health", "Health and wellness events"),
    ENVIRONMENT(13, "environment","Environmental and sustainability events"),
    FAMILY(14,"family", "Family and children's events"),
    ENTERTAINMENT(15, "entertainment","Entertainment and nightlife events");

    private final Integer value;
    private final String name;
    private final String description;

    Category(Integer value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static Category fromValue(Integer value) {
        for (Category categoryValue : Category.values()) {
            if (categoryValue.getValue().equals(value)) {
                return categoryValue;
            }
        }
        throw new IllegalArgumentException("Nieprawidłowa wartość kategorii: " + value);
    }

    public static Category fromName(String name) {
        for (Category status : Category.values()) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Nieprawidłowa nazwa statusu: " + name);
    }

    public static Category fromDescription(String description) {
        for (Category status : Category.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Nieprawidłowa nazwa statusu: " + description);
    }
}

