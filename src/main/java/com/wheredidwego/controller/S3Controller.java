package com.wheredidwego.controller;

import com.wheredidwego.service.S3PresignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final S3PresignedUrlService s3Service;

    @GetMapping("/presignedUrl")
    public Map<String, String> getPresignedUrl(@AuthenticationPrincipal String email){
        String url = s3Service.getS3PresignedUrl(email);

        return Map.of("presignedUrl", url);

    }
}
