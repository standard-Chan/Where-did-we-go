package com.wheredidwego.controller;

import com.wheredidwego.domain.FriendRequest;
import com.wheredidwego.dto.FriendRequestDto;
import com.wheredidwego.dto.ReceivedFriendRequestResponseDto;
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
            return ResponseEntity.status(201).body(friendRequest);
        } catch (FriendRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** 친구 보낸 요청/ 받은 친구 요청 검색 */
    @GetMapping()
    public ResponseEntity<?> searchFriendRequests(@AuthenticationPrincipal CustomUserDetails userDetails) {
        //         @RequestParam("type")String type) {
        try {
            //
            //List<FriendRequest> response = friendRequestService.handleSearchRequest(userDetails, type);
            List<FriendRequest> friendRequestList = friendRequestService.handleSearchRequest(userDetails, "received");
            List<ReceivedFriendRequestResponseDto> response = friendRequestList
                    .stream().map(ReceivedFriendRequestResponseDto::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (FriendRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
