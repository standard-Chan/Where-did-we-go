package com.wheredidwego.controller;

import com.wheredidwego.dto.UploadPresignedDto;
import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/presignedUrl")
    public ResponseEntity<?> getPresignedUrl(@AuthenticationPrincipal CustomUserDetails userDetails){

        String filename = String.valueOf(System.currentTimeMillis()); // filestamp로 이름 지정
        String uploadUrl = s3Service.getUploadS3PresignedUrl(userDetails.getUsername(), filename);

        UploadPresignedDto responseDto = new UploadPresignedDto(uploadUrl, filename);

        return ResponseEntity.ok().body(responseDto);

    }
}
