package com.wheredidwego.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 설명 : 친구 요청 수락 시에 생성되는 entity.
 * 기타
 *  - user, friend 가 중복이 없도록 Unique 제약조건으로 설계
 *  - 단방향으로 친구 요청 수락시 2개의 entity가 생성된다. 확장성을 고려하여 단방향으로 설정.
 */

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 중복된 친구관계 비허가
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"}))
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private User friend;

    @Enumerated(EnumType.STRING)
    private FriendAccessLevel accessLevel;

    public Friend(User user, User friend) {
        this.user = user;
        this.friend = friend;
        this.accessLevel = FriendAccessLevel.LOCATION_ONLY; // 기본 값으로 저장한 좌표만 확인 가능
    }

    public void setAccessLevel(FriendAccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }
}
