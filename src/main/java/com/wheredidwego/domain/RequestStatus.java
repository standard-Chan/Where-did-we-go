package com.wheredidwego.domain;

public enum RequestStatus {
    PENDING("연기"),
    ACCEPTED("수락"),
    REJECTED("거절");

    private final String description;

    RequestStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
