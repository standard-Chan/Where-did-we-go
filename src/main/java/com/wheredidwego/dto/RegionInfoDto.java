package com.wheredidwego.dto;

import lombok.Getter;

@Getter
public class RegionInfoDto {
    private final String province;
    private final String district;
    private final String subdistrict; // 읍면동

    public RegionInfoDto(String province, String district, String subdistrict) {
        this.province = province;
        this.district = district;
        this.subdistrict = subdistrict;
    }
}
