package com.wheredidwego.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoEntryUploadDto {
    private String filename;
    private String description;
    private String takenAt;
    private Double lat;
    private Double lng;
}
