package com.wheredidwego.service;

import com.wheredidwego.domain.FriendRequest;
import com.wheredidwego.domain.User;
import com.wheredidwego.exception.FriendRequestException;
import com.wheredidwego.repository.FriendRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;

    /**
     *  해당 데이터가 없을 수도 있으므로 Optional로 return
     * @param sender
     * @return Optional<FriendRequest>
     */
    public Optional<FriendRequest> getFriendRequestBySender(User sender) {
        return friendRequestRepository.findFriendRequestsBySender(sender);
    }

    public Optional<FriendRequest> getFriendRequestByReceiver(User receiver) {
        return friendRequestRepository.findFriendRequestsBySender(receiver);
    }

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

        // 해당 친구 요청이 이미 존재할 경우
        if (friendRequestRepository.existsFriendRequestBySenderAndReceiver(sender, receiver)) {
            throw new FriendRequestException("친구 요청을 이미 보냈습니다.");
        }

        FriendRequest friendRequest = new FriendRequest(sender, receiver);
        return friendRequestRepository.save(friendRequest);
    }
}
