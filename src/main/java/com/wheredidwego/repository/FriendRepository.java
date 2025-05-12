package com.wheredidwego.repository;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findAllByUser(User user);

    boolean findByUserAndFriend(User user, User friend);

    boolean existsFriendByUserAndFriend(User user, User friend);

    void deleteByUserAndFriend(User user, User friend);
}
