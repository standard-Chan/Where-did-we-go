package com.wheredidwego.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoEntryUploadRequestDto {
    private String filename;
    private String description;
    private String takenAt;
    private Double lat;
    private Double lng;
}
