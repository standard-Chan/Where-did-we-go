package com.wheredidwego.dto;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.Region;
import com.wheredidwego.util.lib.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoEntryResponseDto {
    private String photoUrl;
    private String description;
    private String takenAt;
    private Double lat;
    private Double lng;
    private String province;
    private String district;
    private String subdistrict;

    public PhotoEntryResponseDto(PhotoEntry photoEntry) {
        this.photoUrl = photoEntry.getPhotoUrl();
        this.description = photoEntry.getDescription();
        this.takenAt = Date.localDateToString(photoEntry.getTakenAt());
        Region region = photoEntry.getRegion();
        this.lat = photoEntry.getRegion().getLat();
        this.lng = photoEntry.getRegion().getLng();
        this.province = region.getProvince();
        this.district = region.getDistrict();
        this.subdistrict = region.getSubdistrict();
    }
}
