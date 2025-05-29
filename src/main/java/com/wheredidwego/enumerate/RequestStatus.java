package com.wheredidwego.enumerate;

public enum RequestStatus {
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED");

    private final String description;

    RequestStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
