package com.wheredidwego.controller;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.PhotoEntryResponseDto;
import com.wheredidwego.dto.PhotoEntryUploadDto;
import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.service.PhotoEntryService;
import com.wheredidwego.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PhotoEntryController {

    private final PhotoEntryService photoEntryService;
    private final UserService userService;

    @PostMapping("/photoEntry")
    public ResponseEntity<?> uploadPhotoWithRegion(@RequestBody PhotoEntryUploadDto dto,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {

        String email = userDetails.getUsername();
        if (email == null) {
            throw new RuntimeException("[ERROR]: OAuth2 로그인에 이메일 정보가 없습니다.");
        }

        User user = userService.findUserByEmail(email);
        PhotoEntry photoEntry = photoEntryService.uploadPhotoEntry(dto, user);
        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/photoEntry")
    public ResponseEntity<?> getPhotoEntryById(@RequestParam("id") Long id) {
        PhotoEntry photoEntry = photoEntryService.getPhotoEntryById(id);
        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry);
        return ResponseEntity.ok().body(responseDto);
    }
}
