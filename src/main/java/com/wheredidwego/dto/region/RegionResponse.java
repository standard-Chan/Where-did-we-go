package com.wheredidwego.dto.region;

import com.wheredidwego.domain.Region;
import lombok.Getter;

@Getter
public class RegionResponse {

    private Double lat;  // 위도
    private Double lng;  // 경도

    private String province; // 자치도
    private String district; // 행정 구역 구
    private String subdistrict; //  읍면동

    private Long referenceCount; // 참조 횟수

    public RegionResponse(Region region) {
        this.lat = region.getLat();
        this.lng = region.getLng();

        this.province = region.getProvince();
        this.district = region.getDistrict();
        this.subdistrict = region.getSubdistrict();

        this.referenceCount = region.getReferenceCount();
    }
}
