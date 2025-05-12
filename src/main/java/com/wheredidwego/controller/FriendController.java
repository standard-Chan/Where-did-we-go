package com.wheredidwego.controller;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.friendDto.FriendResponseDto;
import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.service.FriendService;
import com.wheredidwego.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friends")
public class FriendController {
    private final FriendService friendService;
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<?> getFriends(@AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userService.findUserByEmail(userDetails.getUsername());
        Set<Friend> friends = friendService.getFriendsByUser(user);
        List<FriendResponseDto> response = friends.stream().map(FriendResponseDto::new).toList();
        return ResponseEntity.ok().body(response);
    }


}
