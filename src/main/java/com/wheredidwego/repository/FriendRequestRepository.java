package com.wheredidwego.repository;

import com.wheredidwego.domain.FriendRequest;
import com.wheredidwego.domain.enumerate.RequestStatus;
import com.wheredidwego.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findFriendRequestById(Long id);
    
    List<FriendRequest> findFriendRequestsByReceiver(User receiver);
    List<FriendRequest> findFriendRequestsBySender(User sender);

    
    boolean existsFriendRequestBySenderAndReceiver(User sender, User receiver);

    FriendRequest sender(User sender);

    boolean existsFriendRequestBySenderAndReceiverAndStatus(User sender, User receiver, RequestStatus status);

    List<FriendRequest> findFriendRequestsBySenderAndStatus(User sender, RequestStatus status);

    List<FriendRequest> findFriendRequestsByReceiverAndStatus(User receiver, RequestStatus status);
}
