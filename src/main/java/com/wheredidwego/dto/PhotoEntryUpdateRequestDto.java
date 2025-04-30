package com.wheredidwego.dto;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.util.lib.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// PUT 요청 - photo entry update
@Getter
@Setter
@NoArgsConstructor
public class PhotoEntryUpdateRequestDto {
    private Long id;
    private String description;
    private String takenAt; // ISO 표준 : 2024-05-06
    private Double lat;
    private Double lng;
}
