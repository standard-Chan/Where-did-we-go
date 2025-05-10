package com.wheredidwego.controller;

import com.wheredidwego.domain.FriendRequest;
import com.wheredidwego.dto.*;
import com.wheredidwego.dto.friendDto.FriendRequestDecisionDto;
import com.wheredidwego.dto.friendDto.FriendRequestDecisionResponseDto;
import com.wheredidwego.dto.friendDto.FriendRequestDto;
import com.wheredidwego.dto.friendDto.FriendRequestResponseDto;
import com.wheredidwego.exception.FriendRequestException;
import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends-request")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @PostMapping()
    public ResponseEntity<?> sendFriendRequest(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestBody FriendRequestDto requestDto) {

        try {
            FriendRequest friendRequest = friendRequestService.handleRequest(userDetails, requestDto.getFriendEmail());
            FriendRequestResponseDto response = new FriendRequestResponseDto(friendRequest);
            return ResponseEntity.status(201).body(response);
        } catch (FriendRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** 친구 보낸 요청, 받은 친구 요청 검색 */
    @GetMapping()
    public ResponseEntity<?> searchFriendRequests(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestParam("type")String type) {
        try {
            List<FriendRequest> friendRequestList = friendRequestService.handleSearchRequest(userDetails, type);

            // 받은 친구 요청 조회
            if (type.equalsIgnoreCase("RECEIVED")) {
            List<ReceivedFriendRequestResponseDto> response = friendRequestList
                    .stream().map(ReceivedFriendRequestResponseDto::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
            }
            // 보낸 친구 요청 조회
            else {
                List<FriendRequestResponseDto> response = friendRequestList
                        .stream().map(FriendRequestResponseDto::new)
                        .collect(Collectors.toList());
                return ResponseEntity.ok().body(response);
            }
        } catch (FriendRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 친구 요청 수락/거절 처리
    @PatchMapping()
    public ResponseEntity<?> responseToRequest(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestBody FriendRequestDecisionDto requestDto) {
        try {
            FriendRequest friendRequest = friendRequestService.FriendRequestDecisionHandler(userDetails, requestDto.getFriendRequestId(), requestDto.getStatus());
            FriendRequestDecisionResponseDto response = new FriendRequestDecisionResponseDto(friendRequest);

            return ResponseEntity.ok().body(response);
        } catch (FriendRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
