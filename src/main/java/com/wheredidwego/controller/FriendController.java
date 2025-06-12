package com.wheredidwego.controller;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.friendDto.FriendResponseDto;
import com.wheredidwego.dto.friendDto.FriendUpdateRequest;
import com.wheredidwego.dto.friendDto.FriendUpdateResponse;
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
    public ResponseEntity<List<FriendResponseDto>> getFriends(@AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userService.findUserByUserDetails(userDetails);
        Set<Friend> friends = user.getFriends();
        List<FriendResponseDto> response = friends.stream().map(FriendResponseDto::new).toList();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{friendEntityId}")
    public ResponseEntity<FriendUpdateResponse> updateFriendInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PathVariable("friendEntityId") Long friendEntityId,
                                                                @RequestBody FriendUpdateRequest friendUpdateRequest) {
        User user = userService.findUserByUserDetails(userDetails);

        Friend friend = friendService.getFriendById(friendEntityId);
        Friend updatedFriend = friendService.updateFriendInfo(user, friend, friendUpdateRequest.getAccessLevel(), friendUpdateRequest.getDescription());
        FriendUpdateResponse response = new FriendUpdateResponse(updatedFriend);

        return ResponseEntity.ok(response);
    }

    // 설계 참고 (양방향을 모두 제거해야하므로 frinedEntityId가 아닌 friendEmail을 통해 삭제)
    @DeleteMapping("/{friendId}")
    public ResponseEntity<String> deleteFriend(@PathVariable("friendId") Long friendId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userService.findUserByUserDetails(userDetails);
        User friendUser = userService.findUserById(friendId);

        friendService.deleteFriend(user, friendUser);

        return ResponseEntity.ok().body("삭제하였습니다.");
    }
}
