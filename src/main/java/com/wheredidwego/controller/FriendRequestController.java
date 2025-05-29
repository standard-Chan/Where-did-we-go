package com.wheredidwego.controller;

import com.wheredidwego.domain.FriendRequest;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.friendDto.*;
import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.service.FriendRequestService;
import com.wheredidwego.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends-request")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;
    private final UserService userService;

    @PostMapping()
    public ResponseEntity<?> sendFriendRequest(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestBody FriendRequestDto requestDto) {
        User user = userService.findUserByUserDetails(userDetails);
        User receiver = userService.findUserByEmail(requestDto.getFriendEmail());

        FriendRequest friendRequest = friendRequestService.createFriendRequest(user, receiver);
        SentFriendRequestResponseDto response = new SentFriendRequestResponseDto(friendRequest);
        return ResponseEntity.status(201).body(response);

    }

    /** 받은 친구 요청 조회 */
    @GetMapping("/received")
    public ResponseEntity<List<ReceivedFriendRequestResponseDto>> searchReceivedFriendRequest(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findUserByUserDetails(userDetails);

        List<FriendRequest> friendRequestList = friendRequestService.searchReceivedRequest(user);

        // 받은 친구 요청 조회
        List<ReceivedFriendRequestResponseDto> response = friendRequestService.mapFriendRequestsToDto(friendRequestList, ReceivedFriendRequestResponseDto::new);

        return ResponseEntity.ok().body(response);
    }

    /** 보낸 친구 요청 조회 */
    @GetMapping("/sent")
    public ResponseEntity<List<SentFriendRequestResponseDto>> searchSentFriendRequests(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findUserByUserDetails(userDetails);

        List<FriendRequest> friendRequestList = friendRequestService.searchReceivedRequest(user);

        List<SentFriendRequestResponseDto> response = friendRequestService.mapFriendRequestsToDto(friendRequestList, SentFriendRequestResponseDto::new);

        return ResponseEntity.ok().body(response);
    }

    // 친구 요청 수락/거절 처리
    @PatchMapping()
    public ResponseEntity<?> updateStatus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestBody FriendRequestDecisionDto requestDto) {

            FriendRequest friendRequest = friendRequestService.FriendRequestDecisionHandler(userDetails, requestDto.getFriendRequestId(), requestDto.getStatus());
            FriendRequestDecisionResponseDto response = new FriendRequestDecisionResponseDto(friendRequest);

            return ResponseEntity.ok().body(response);
    }
}
