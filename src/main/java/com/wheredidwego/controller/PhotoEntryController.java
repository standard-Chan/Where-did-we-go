package com.wheredidwego.controller;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.PhotoEntryResponseDto;
import com.wheredidwego.dto.PhotoEntryUploadDto;
import com.wheredidwego.repository.PhotoEntryRepository;
import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.service.PhotoEntryService;
import com.wheredidwego.service.S3Service;
import com.wheredidwego.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photo-entries")
@RequiredArgsConstructor
public class PhotoEntryController {

    private final PhotoEntryService photoEntryService;
    private final UserService userService;
    private final S3Service s3Service;
    private final PhotoEntryRepository photoEntryRepository;

    @PostMapping()
    public ResponseEntity<?> uploadPhotoWithRegion(@RequestBody PhotoEntryUploadDto dto,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();

        // user 데이터 가져오기
        User user = userService.findUserByEmail(email);
        PhotoEntry photoEntry = photoEntryService.uploadPhotoEntry(dto, user);

        // response dto 생성 및 반환
        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry);
        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping()
    public ResponseEntity<?> getPhotoEntryById(@PathVariable("id") Long id) {
        PhotoEntry photoEntry = photoEntryService.getPhotoEntryById(id);

        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry);
        // s3 이미지 다운로드 presigned url
        responseDto.setPhotoUrl(s3Service.getDownloadS3PresignedUrl(id));
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyPhotoEntries(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();

        User user = userService.findUserByEmail(userDetails.getUsername());
        List<PhotoEntry> photoEntries = photoEntryRepository.findAllByUser(user);

        List<PhotoEntryResponseDto> responseDtos = photoEntries
                .stream()
                .map(photoEntry -> {
                    PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry);
                    responseDto.setPhotoUrl(s3Service.getDownloadS3PresignedUrl(photoEntry.getId()));
                    return responseDto;
                }).toList();

        return ResponseEntity.ok().body(responseDtos);
    }

    @DeleteMapping("/{photoEntryId}")
    public ResponseEntity<?> deleteImageById(@PathVariable("photoEntryId") Long photoEntryId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        PhotoEntry photoEntry = photoEntryService.getPhotoEntryById(photoEntryId);

        photoEntryService.deletePhotoEntryById(photoEntryId, userDetails);

        return ResponseEntity.ok().body("삭제되었습니다.");
    }
}
