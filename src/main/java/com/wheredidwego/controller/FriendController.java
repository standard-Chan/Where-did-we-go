package com.wheredidwego.controller;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.friendDto.FriendResponseDto;
import com.wheredidwego.dto.friendDto.FriendUpdateDto;
import com.wheredidwego.exception.FriendException;
import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.service.FriendService;
import com.wheredidwego.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

        try {
            User user = userService.findUserByEmail(userDetails.getUsername());
            Set<Friend> friends = friendService.getFriendsSetByUser(user);
            List<FriendResponseDto> response = friends.stream().map(FriendResponseDto::new).toList();
            return ResponseEntity.ok().body(response);
        } catch (FriendException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping()
    public ResponseEntity<?> updateFriendInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @RequestBody FriendUpdateDto friendUpdateDto) {
        try {
            Friend friend = friendService.getFriendById(friendUpdateDto.getFriendEntityId());
            Friend updatedFriend = friendService.updateFriend(friend, friendUpdateDto.getAccessLevel(), friendUpdateDto.getDescription());
            FriendUpdateDto responseDto = new FriendUpdateDto(updatedFriend);
            return ResponseEntity.ok(responseDto);
        } catch (FriendException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{friendEmail}")
    public ResponseEntity<?> deleteFriend(@PathVariable("friendEmail") String friendEmail,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            User user = userService.findUserByEmail(userDetails.getUsername());
            User friendUser = userService.findUserByEmail(friendEmail);

            friendService.deleteFriend(user, friendUser);

            return ResponseEntity.ok().build();
        } catch (FriendException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
