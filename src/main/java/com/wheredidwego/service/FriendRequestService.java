package com.wheredidwego.service;

import com.wheredidwego.domain.FriendRequest;
import com.wheredidwego.domain.User;
import com.wheredidwego.exception.FriendRequestException;
import com.wheredidwego.repository.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
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

        // 해당 친구 요청이 이미 존재할 경우
        if (friendRequestRepository.existsFriendRequestBySenderAndReceiver(sender, receiver)) {
            throw new FriendRequestException("친구 요청을 이미 보냈습니다.");
        }

        FriendRequest friendRequest = new FriendRequest(sender, receiver);
        return friendRequestRepository.save(friendRequest);
    }

    /**
     * userDetails를 친구요청 전송 목록, 혹은 친구 요청을 받은 목록 검색을 위한 중간역할 (도메인 객체 변환 + 내부 로직 호출)
     * @param userDetails 친구 요청 검색 대상자
     * @param type 확인할 요청 타입 ( sent / received )
     * @return
     */
    public List<FriendRequest> handleSearchRequest(UserDetails userDetails, String type) {
        User user = userService.findUserByEmail(userDetails.getUsername());

        return SearchRequestByType(user, type);
    }

    /**
     * 친구 요청 전송 목록, 혹은 친구 요청을 받은 목록 검색
     * @param user
     * @param type
     * @return 친구 요청 목록
     */
    public List<FriendRequest> SearchRequestByType(User user, String type) {
        if (type.equalsIgnoreCase("SENT")) {
            return friendRequestRepository.findFriendRequestsBySender(user);
        } else if (type.equalsIgnoreCase("RECEIVED")) {
            return friendRequestRepository.findFriendRequestsByReceiver(user);
        }
        throw new FriendRequestException("잘못된 param TYPE이 전달되었습니다. (type은 sent 혹은 received 이어야 합니다.)");
    }

}
