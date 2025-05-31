package com.wheredidwego.dto.photoEntry;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// PUT 요청 - photo entry update
@Getter
@Setter
@NoArgsConstructor
public class PhotoEntryUpdateRequestDto {
    private String description;
    private String takenAt; // ISO 표준 : 2024-05-06
    private Double lat;
    private Double lng;
}
