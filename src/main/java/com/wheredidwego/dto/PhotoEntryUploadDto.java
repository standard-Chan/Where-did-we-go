package com.wheredidwego.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PhotoEntryUploadDto {
    private String photoUrl;
    private String description;
    private LocalDate takenAt;
    private Double lat;
    private Double lng;
}
