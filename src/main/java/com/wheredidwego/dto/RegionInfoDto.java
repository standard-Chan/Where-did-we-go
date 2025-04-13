package com.wheredidwego.dto;

import lombok.Getter;

@Getter
public class RegionInfoDto {
    private final String province;
    private final String district;

    public RegionInfoDto(String province, String district) {
        this.province = province;
        this.district = district;
    }
}
