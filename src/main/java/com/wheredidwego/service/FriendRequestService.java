package com.wheredidwego.service;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.FriendRequest;
import com.wheredidwego.enumerate.RequestStatus;
import com.wheredidwego.domain.User;
import com.wheredidwego.exception.ErrorCode;
import com.wheredidwego.exception.FriendRequestException;
import com.wheredidwego.repository.FriendRepository;
import com.wheredidwego.repository.FriendRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final UserService userService;

    /**
     * 중간 역할 (도메인 객체 변환 + 내부 로직 호출)
     * @param userDetails sender
     * @param receiverEmail 친구요청을 받는 사람 이메일
     * @return
     */
    public FriendRequest handleRequest(UserDetails userDetails, String receiverEmail) {
        User sender = userService.findUserByEmail(userDetails.getUsername());
        User receiver = userService.findUserByEmail(receiverEmail);

        return createFriendRequest(sender, receiver);
    }
    /**
     * 친구 요청 생성
     * @param sender 요청자
     * @param receiver
     * @return  생성된 친구 요청 정보
     */
    public FriendRequest createFriendRequest(User sender, User receiver) {
        // 예외 처리
        // 해당 user와 이미 친구일 경우
        if (friendRepository.existsFriendByUserAndFriend(sender, receiver)) {
            throw new FriendRequestException(ErrorCode.ALREADY_FRIEND);
        }

        // 해당 친구 요청을 이미 보낸 경우 (보류된 경우)
        if (friendRequestRepository.existsFriendRequestBySenderAndReceiverAndStatus(sender, receiver, RequestStatus.PENDING)) {
            throw new FriendRequestException(ErrorCode.REQUEST_ALREADY_SENT);
        }

        FriendRequest friendRequest = new FriendRequest(sender, receiver);
        return friendRequestRepository.save(friendRequest);
    }

    /**
     * 받은 친구 요청 전송 목록 조회
     */
    public List<FriendRequest> getReceivedRequest(User user) {
        return friendRequestRepository.findFriendRequestsByReceiverAndStatus(user, RequestStatus.PENDING);
    }

    public FriendRequest searchRequestById(Long id) {
        return friendRequestRepository.findFriendRequestById(id)
                .orElseThrow(() -> new FriendRequestException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));
    }

    /**
     * Controller 와 메인로직(친구 요청 처리) 사이 중간 처리 역할
     * @param userDetails
     * @param friendRequestId
     * @param status (ACCEPTED, REJECTED)
     */
    public FriendRequest FriendRequestDecisionHandler(UserDetails userDetails, Long friendRequestId, RequestStatus status) {
        FriendRequest friendRequest = searchRequestById(friendRequestId);
        User requestReceiver = userService.findUserByEmail(userDetails.getUsername());

        // 요청 수락자와 현재 로그인한 유저가 동일한 지 검사
        if (! (requestReceiver == friendRequest.getReceiver())) {
            throw new FriendRequestException(ErrorCode.NO_PERMISSION_TO_DECIDE);
        }

        // 수락 시
        if (status == RequestStatus.ACCEPTED) {
            return acceptFriendRequest(friendRequest);
        }
        // 거절 시
        else if (status == RequestStatus.REJECTED) {
            return rejectFriendRequest(friendRequest);
        } else {
            throw new FriendRequestException(ErrorCode.INVALID_STATUS);
        }
    }

    /**
     * 친구 요청 수락.
     * 단방향 Frined Entity 2개 생성하고, FriendReuqest의 status를 ACCEPTED롤 변경한다.
     * @param friendRequest 친구 요청 정보
     * @return
     */
    public FriendRequest acceptFriendRequest(FriendRequest friendRequest){
        User receiver = friendRequest.getReceiver();
        User sender = friendRequest.getSender();

        // 친구 요청 Entity 업데이트
        friendRequest.setStatus(RequestStatus.ACCEPTED);
        friendRequest.setRespondedAt(LocalDateTime.now());

        // 단방향 Friend Entity 2개 생성
        Friend receiverToSenderFriendship = new Friend(receiver, sender);
        Friend senderToReceiverFriendship = new Friend(sender, receiver);

        // 저장
        friendRepository.save(receiverToSenderFriendship);
        friendRepository.save(senderToReceiverFriendship);

        receiver.addFriend(receiverToSenderFriendship);
        sender.addFriend(senderToReceiverFriendship);

        return friendRequest;
    }

    /**
     * 친구 요청 거절. FriendRequest의 status를 REJECTED로 변경한다.
     * @param friendRequest 친구 요청 정보
     * @return
     */
    public FriendRequest rejectFriendRequest(FriendRequest friendRequest) {
        friendRequest.setStatus(RequestStatus.REJECTED);
        friendRequest.setRespondedAt(LocalDateTime.now());
        return friendRequest;
    }

}
