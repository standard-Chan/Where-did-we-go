package com.wheredidwego.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoEntryUploadDto {
    private String photoUrl;
    private String description;
    private String takenAt;
    private Double lat;
    private Double lng;
}
