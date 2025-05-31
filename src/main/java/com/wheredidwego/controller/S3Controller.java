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
@RequestMapping("/api/v1/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/presignedUrl")
    public ResponseEntity<?> getPresignedUrl(@AuthenticationPrincipal CustomUserDetails userDetails){

        // filename 지정: timestamp로 파일명 겹치지 않도록 설정
        String filename = String.valueOf(System.currentTimeMillis());
        String uploadUrl = s3Service.getUploadS3PresignedUrl(userDetails.getUsername(), filename);

        UploadPresignedDto responseDto = new UploadPresignedDto(uploadUrl, filename);

        return ResponseEntity.ok().body(responseDto);

    }
}
