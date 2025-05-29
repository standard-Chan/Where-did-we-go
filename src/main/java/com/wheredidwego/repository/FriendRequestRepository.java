package com.wheredidwego.repository;

import com.wheredidwego.domain.FriendRequest;
import com.wheredidwego.enumerate.RequestStatus;
import com.wheredidwego.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findFriendRequestById(Long id);

    boolean existsFriendRequestBySenderAndReceiverAndStatus(User sender, User receiver, RequestStatus status);

    List<FriendRequest> findFriendRequestsByReceiverAndStatus(User receiver, RequestStatus status);
}
