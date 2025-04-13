package com.wheredidwego.domain;

import com.wheredidwego.dto.RegionInfoDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double lat;  // 위도
    @Column(nullable = false)
    private Double lng;  // 경도

    private String province; // 자치도
    private String district; // 행정 구역 구

    @Column(nullable = false)
    private Long referenceCount; // 참조 횟수

    public Region(Double lat, Double lng, RegionInfoDto regionInfo) {
        this.lat = lat;
        this.lng = lng;
        this.province = regionInfo.getProvince();
        this.district = regionInfo.getDistrict();
        this.referenceCount = 1L;
    }

    public void increaseReferenceCount() {
        this.referenceCount++;
    }

}
