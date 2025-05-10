package com.wheredidwego.domain;

/**
 * 친구 사이 정보 열람 권한
 */
public enum FriendAccessLevel {
    NONE,   // 권한없음
    LOCATION_ONLY,  // 조회만 가능
    VIEW_DETAIL,   // 디테일한 정보 조회 가능
    FULL_ACCESS,    // 수정, 삭제 등 모든 권한 가능
}
