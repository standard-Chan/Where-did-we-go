package com.wheredidwego.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProvincePhotoCountResponse {
    private String province;
    private Long count;

}
