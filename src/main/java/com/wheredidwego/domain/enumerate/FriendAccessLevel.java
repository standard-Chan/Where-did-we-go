package com.wheredidwego.domain.enumerate;

/**
 * 친구 사이 정보 열람 권한
 */
public enum FriendAccessLevel {
    NONE("권한 없음"),
    LOCATION_ONLY("위치 조회 가능"),
    VIEW_DETAIL("세부 정보 조회 가능"),
    FULL_ACCESS( "모든 접근 가능");

    private final String description;

    FriendAccessLevel(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
}
