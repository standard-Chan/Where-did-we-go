package com.wheredidwego.controller.photoEntry;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.photoEntry.PhotoEntryResponseDto;
import com.wheredidwego.dto.photoEntry.PhotoEntryUpdateRequestDto;
import com.wheredidwego.dto.photoEntry.PhotoEntryUploadRequestDto;
import com.wheredidwego.dto.photoEntry.ProvincePhotoCountResponse;
import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.service.PhotoEntryMapper;
import com.wheredidwego.service.PhotoEntryService;
import com.wheredidwego.service.S3Service;
import com.wheredidwego.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/photo-entries")
@RequiredArgsConstructor
public class PhotoEntryController {

    private final PhotoEntryService photoEntryService;
    private final UserService userService;
    private final S3Service s3Service;
    private final PhotoEntryMapper photoEntryMapper;

    @PostMapping()
    public ResponseEntity<PhotoEntryResponseDto> uploadPhotoWithRegion(@RequestBody PhotoEntryUploadRequestDto dto,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userService.findUserByUserDetails(userDetails);
        PhotoEntry photoEntry = photoEntryService.uploadPhotoEntry(dto, user);
        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry, s3Service);

        return ResponseEntity.status(201).body(responseDto);
    }

    // 해당 id의 photo entry 반환
    @GetMapping("/{id}")
    public ResponseEntity<PhotoEntryResponseDto> getPhotoEntryById(@PathVariable("id") Long id) {

        PhotoEntry photoEntry = photoEntryService.getPhotoEntryById(id);
        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry, s3Service);

        return ResponseEntity.ok().body(responseDto);
    }


    // photo entry list 반환
    @GetMapping()
    public ResponseEntity<List<PhotoEntryResponseDto>> getPhotoEntries(@RequestParam(value = "swLat", defaultValue = "33")double swLat,
                                               @RequestParam(value = "swLng", defaultValue = "124")double swLng,
                                               @RequestParam(value = "neLat", defaultValue = "43")double neLat,
                                               @RequestParam(value = "neLng", defaultValue = "132")double neLng,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userService.findUserByUserDetails(userDetails);
        List<PhotoEntry> photoEntries = photoEntryService.getPhotoEntriesInBounds(user, swLat, swLng, neLat, neLng);
        List<PhotoEntryResponseDto> response = photoEntryMapper.mapToDtoList(photoEntries);

        return ResponseEntity.ok().body(response);
    }

    // photo entry 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePhotoEntryById(@PathVariable("id") Long id,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findUserByUserDetails(userDetails);
        photoEntryService.deletePhotoEntryById(id, user);
        return ResponseEntity.ok().body("성공적으로 삭제되었습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhotoEntryResponseDto> updatePhotoEntryById(@PathVariable("id") Long id,
                                            @RequestBody PhotoEntryUpdateRequestDto requestDto) {

        PhotoEntry photoEntry = photoEntryService.updatePhotoEntry(id, requestDto);
        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry, s3Service);

        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/statistics/province")
    public ResponseEntity<List<ProvincePhotoCountResponse>> getPhotoStatisticsByProvince(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userService.findUserByUserDetails(userDetails);
        List<ProvincePhotoCountResponse> response = photoEntryService.getPhotoEntryStatisticsByProvince(user);
        return ResponseEntity.ok().body(response);
    }

//    @GetMapping("/native")
//    public ResponseEntity<List<PhotoEntryResponseDto>> getPhotoEntriesWithNativeQuery(@RequestParam(value = "swLat", defaultValue = "33")double swLat,
//                                                                         @RequestParam(value = "swLng", defaultValue = "124")double swLng,
//                                                                         @RequestParam(value = "neLat", defaultValue = "43")double neLat,
//                                                                         @RequestParam(value = "neLng", defaultValue = "132")double neLng,
//                                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
//
//        User user = userService.findUserByUserDetails(userDetails);
//        List<PhotoEntryWithRegionDto> photoEntries = photoEntryService.getPhotoEntryWithRegionDto(user, swLat, swLng, neLat, neLng);
//        List<PhotoEntryResponseDto> response = photoEntryMapper.photoEntryWithRegionMapToDtoList(photoEntries);
//        return ResponseEntity.ok().body(response);
//    }

}
