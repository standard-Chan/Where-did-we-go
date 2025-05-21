package com.wheredidwego.controller;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.PhotoEntryResponseDto;
import com.wheredidwego.dto.PhotoEntryUpdateRequestDto;
import com.wheredidwego.dto.PhotoEntryUploadRequestDto;
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
    public ResponseEntity<?> uploadPhotoWithRegion(@RequestBody PhotoEntryUploadRequestDto dto,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        // 해당 user의 photo entry 데이터 가져오기
        String email = userDetails.getUsername();
        User user = userService.findUserByEmail(email);
        PhotoEntry photoEntry = photoEntryService.uploadPhotoEntry(dto, user);

        // response dto 생성 및 반환
        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry);
        responseDto.setPhotoUrl(s3Service.getDownloadS3PresignedUrl(photoEntry.getPhotoPath()));
        return ResponseEntity.status(201).body(responseDto);
    }

    // 해당 id의 photo entry 반환
    @GetMapping("/{id}")
    public ResponseEntity<?> getPhotoEntryById(@PathVariable("id") Long id) {

        PhotoEntry photoEntry = photoEntryService.getPhotoEntryById(id);
        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry);
        // s3 이미지 다운로드 presigned url
        responseDto.setPhotoUrl(s3Service.getDownloadS3PresignedUrl(photoEntry.getPhotoPath()));
        return ResponseEntity.ok().body(responseDto);
    }

    // photo entry list 반환
    @GetMapping()
    public ResponseEntity<?> getMyPhotoEntries(@RequestParam(value = "swLat", defaultValue = "takenAt")double swLat,
                                               @RequestParam(value = "swLng", defaultValue = "desc")double swLng,
                                               @RequestParam(value = "neLat", defaultValue = "0")double neLat,
                                               @RequestParam(value = "neLng", defaultValue = "10")double neLng,
                                               @RequestParam(value = "level", defaultValue = "5")int level,
                                               @RequestParam(value = "page", defaultValue = "0")int page,
                                               @RequestParam(value = "size", defaultValue = "500")int size,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userService.findUserByEmail(userDetails.getUsername());
        List<PhotoEntry> photoEntries = photoEntryService.getPhotoEntriesInBounds(user, swLat, swLng, neLat, neLng);

        List<PhotoEntryResponseDto> response = photoEntryService.wrappingPhotoEntry2Response(photoEntries);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPhotoEntries(@AuthenticationPrincipal CustomUserDetails userDetails) {

        long start = System.currentTimeMillis();

        // 해당 User의 사진 정보 가져오기
        User user = userService.findUserByEmail(userDetails.getUsername());
        List<PhotoEntry> photoEntries = photoEntryRepository.findAllByUser(user);

        long m = System.currentTimeMillis();
        System.out.println("DB query 소요 시간 : " + (m - start) + "ms");

        // 해당 User의 Photo entry 목록을 dto에 저장
        List<PhotoEntryResponseDto> responseDtos = photoEntryService.wrappingPhotoEntry2Response(photoEntries);

        long end = System.currentTimeMillis();
        System.out.println("stream 소요 시간 : " + (end - m) + "ms");

        return ResponseEntity.ok().body(responseDtos);
    }

    // photo entry 삭제
    @DeleteMapping("/{photoEntryId}")
    public ResponseEntity<?> deletePhotoEntryById(@PathVariable("photoEntryId") Long photoEntryId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        PhotoEntry photoEntry = photoEntryService.getPhotoEntryById(photoEntryId);
        photoEntryService.deletePhotoEntryById(photoEntryId, userDetails);

        return ResponseEntity.ok().body("삭제되었습니다.");
    }

    @PutMapping()
    public ResponseEntity<?> updatePhotoEntryById(@RequestBody PhotoEntryUpdateRequestDto requestDto) {

        PhotoEntry photoEntry = photoEntryService.updatePhotoEntry(requestDto);
        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry);

        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/friends/{friend}")
    public ResponseEntity<?> getFriendsPhotoEntries(@PathVariable("friend")String friendEmail,
                                                    @RequestParam(value = "swLat", defaultValue = "takenAt")double swLat,
                                                    @RequestParam(value = "swLng", defaultValue = "desc")double swLng,
                                                    @RequestParam(value = "neLat", defaultValue = "0")double neLat,
                                                    @RequestParam(value = "neLng", defaultValue = "10")double neLng,
                                                    @RequestParam(value = "level", defaultValue = "5")int level,
                                                    @RequestParam(value = "page", defaultValue = "0")int page,
                                                    @RequestParam(value = "size", defaultValue = "500")int size,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        User friend = userService.findUserByEmail(friendEmail);

        // 친구 photo entry 얻기
        List<PhotoEntryResponseDto> response = photoEntryService.getFriendsPhotoEntries(user, friend, swLat, swLng, neLat, neLng);
        return ResponseEntity.ok().body(response);
    }
}
