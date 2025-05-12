package com.wheredidwego.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String nickname;

    // 실제 DB에는 User Entity에 저장되지 않지만, JPA에서 User를 불러올 때, DB의 friend Entity를 훑어보면서 확인한다.
    // mappedBy = "user" 는 Friend entity의 user 필드를 의미한다.
    // cascade 는 User entity를 삭제하면 Friend도 같이 삭제되도록 만든다.
    // orphanRemoval 은 부모인 Set<>friends 의 Friend가 삭제될 경우, 여기의 User id를 외래키로 갖는 Friend Entity도 삭제됨을 의미.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Friend> friends;

    @CreationTimestamp
    private Timestamp createdAt;

    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void addFriend(Friend friend) {
        this.friends.add(friend);
    }
}
