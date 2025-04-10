package com.wheredidwego.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private Double lat;  // 위도
    private Double lng;  // 경도

    private String province; // 자치도
    private String district; // 행정구역 구


    public Region(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
