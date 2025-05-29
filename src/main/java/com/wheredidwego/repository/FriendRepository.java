package com.wheredidwego.repository;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    boolean existsFriendByUserAndFriend(User user, User friend);

    Optional<Friend> getFriendsById(Long id);

    Optional<Friend> findFriendByUserAndFriend(User user, User friend);
}
