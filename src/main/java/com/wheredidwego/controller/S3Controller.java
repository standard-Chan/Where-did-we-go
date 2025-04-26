package com.wheredidwego.controller;

import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.service.S3Service;
import lombok.RequiredArgsConstructor;
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
    public Map<String, String> getPresignedUrl(@AuthenticationPrincipal CustomUserDetails userDetails){
        String uploadUrl = s3Service.getUploadS3PresignedUrl(userDetails.getUsername());

        return Map.of("presignedUrl", uploadUrl);

    }
}
