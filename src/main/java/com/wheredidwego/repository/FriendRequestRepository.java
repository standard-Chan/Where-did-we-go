package com.wheredidwego.repository;

import com.wheredidwego.domain.FriendRequest;
import com.wheredidwego.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findFriendRequestsByReceiver(User receiver);
    Optional<FriendRequest> findFriendRequestsBySender(User sender);

    boolean existsFriendRequestBySenderAndReceiver(User sender, User receiver);
}
