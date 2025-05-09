package com.wheredidwego.controller;

import com.wheredidwego.domain.FriendRequest;
import com.wheredidwego.dto.FriendRequestDto;
import com.wheredidwego.exception.FriendRequestException;
import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
