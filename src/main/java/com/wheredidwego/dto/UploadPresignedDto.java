package com.wheredidwego.dto;

import lombok.Getter;

@Getter
public class UploadPresignedDto {
    private String presignedUrl;
    private String filename;

    public UploadPresignedDto(String presignedUrl, String filename) {
        this.presignedUrl = presignedUrl;
        this.filename = filename;
    }
}
