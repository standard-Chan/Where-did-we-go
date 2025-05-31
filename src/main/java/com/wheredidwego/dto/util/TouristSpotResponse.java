package com.wheredidwego.dto.util;

import com.wheredidwego.domain.TouristSpot;
import lombok.Getter;

@Getter
public class TouristSpotResponse {
    private String name;
    private String categoryCode;
    private String describe;

    private double lat;
    private double lng;

    public TouristSpotResponse(TouristSpot touristSpot) {
        this.name = touristSpot.getName();
        this.lat = touristSpot.getLat();
        this.lng = touristSpot.getLng();
        this.categoryCode = touristSpot.getCategoryCode().getCode();
        this.describe = touristSpot.getCategoryCode().getDescription();
    }
}
