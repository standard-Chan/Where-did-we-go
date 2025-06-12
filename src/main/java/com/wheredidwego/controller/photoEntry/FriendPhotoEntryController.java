package com.wheredidwego.controller.photoEntry;


import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.photoEntry.PhotoEntryResponseDto;
import com.wheredidwego.dto.photoEntry.PhotoEntryUpdateRequestDto;
import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{friendId}/photo-entries") // friendId 는 Friend User의 id임. friendship 아님
@RequiredArgsConstructor
public class FriendPhotoEntryController {
    private final PhotoEntryService photoEntryService;
    private final UserService userService;
    private final S3Service s3Service;
    private final PhotoEntryMapper photoEntryMapper;
    private final FriendService friendService;


    @GetMapping("/{id}")
    public ResponseEntity<PhotoEntryResponseDto> getFriendPhotoEntryById(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId,
                                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        User friend = userService.findUserById(friendId);
        User user = userService.findUserByUserDetails(userDetails);

        PhotoEntry photoEntry = photoEntryService.getPhotoEntryById(id, user, friend);
        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry, s3Service);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping()
    public ResponseEntity<List<PhotoEntryResponseDto>> getPhotoEntriesByFriend(@PathVariable("friendId") Long friendId,
                                                                               @RequestParam(value = "swLat", defaultValue = "33")double swLat,
                                                                               @RequestParam(value = "swLng", defaultValue = "124")double swLng,
                                                                               @RequestParam(value = "neLat", defaultValue = "43")double neLat,
                                                                               @RequestParam(value = "neLng", defaultValue = "132")double neLng,
                                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findUserByUserDetails(userDetails);
        User friend = userService.findUserById(friendId);
        friendService.checkFriendShip(user, friend);

        List<PhotoEntryResponseDto> response = photoEntryService.getFriendsPhotoEntries(user, friend, swLat, swLng, neLat, neLng);
        return ResponseEntity.ok().body(response);
    }

    // photo entry 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePhotoEntryById(@PathVariable("friendId") Long friendId, @PathVariable("id") Long id,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findUserByUserDetails(userDetails);
        User friend = userService.findUserById(friendId);
        friendService.checkFriendShip(user, friend);

        photoEntryService.deletePhotoEntryById(id, user, friend);
        return ResponseEntity.ok().body("성공적으로 삭제되었습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhotoEntryResponseDto> updatePhotoEntryById(@PathVariable("friendId") Long friendId, @PathVariable("id") Long id,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @RequestBody PhotoEntryUpdateRequestDto requestDto) {
        User user = userService.findUserByUserDetails(userDetails);
        User friend = userService.findUserById(friendId);
        friendService.checkFriendShip(user, friend);

        PhotoEntry photoEntry = photoEntryService.updatePhotoEntry(id, requestDto);
        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry, s3Service);

        return ResponseEntity.status(200).body(responseDto);
    }
}
