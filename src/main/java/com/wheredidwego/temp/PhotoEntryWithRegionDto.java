package com.wheredidwego.temp;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PhotoEntryWithRegionDto {
    private Long Id;
    private String photoPath;
    private String description;
    private LocalDate takenAt;
    private Double lat;
    private Double lng;
    private String province;
    private String district;
    private String subdistrict;
}
