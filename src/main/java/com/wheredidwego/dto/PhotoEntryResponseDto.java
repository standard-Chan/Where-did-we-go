package com.wheredidwego.dto;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.Region;
import com.wheredidwego.service.S3Service;
import com.wheredidwego.util.lib.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoEntryResponseDto {
    private Long id;
    @Setter
    private String photoUrl;
    private String description;
    private String takenAt; // ISO 표준 : 2024-05-06
    private Double lat;
    private Double lng;
    private String province;
    private String district;
    private String subdistrict;

    public PhotoEntryResponseDto(PhotoEntry photoEntry, S3Service s3Service) {
        this.id = photoEntry.getId();
        this.description = photoEntry.getDescription();
        this.takenAt = DateUtil.localDateToString(photoEntry.getTakenAt());
        this.photoUrl = s3Service.getDownloadS3PresignedUrl(photoEntry.getPhotoPath());
        Region region = photoEntry.getRegion();
        this.lat = photoEntry.getRegion().getLat();
        this.lng = photoEntry.getRegion().getLng();
        this.province = region.getProvince();
        this.district = region.getDistrict();
        this.subdistrict = region.getSubdistrict();
    }

    /**
     * Region 정보만 담은 photo entry Response 생성. for ONLY_LOCATION_VIEW 권한인 친구 정보 조회
     * @param photoEntry
     */
    public void setPhotoEntryResponseDtoForRegion(PhotoEntry photoEntry) {
        this.id = photoEntry.getId();
        this.description = "false";
        this.takenAt = "false";

        Region region = photoEntry.getRegion();
        this.lat = photoEntry.getRegion().getLat();
        this.lng = photoEntry.getRegion().getLng();
        this.province = region.getProvince();
        this.district = region.getDistrict();
        this.subdistrict = region.getSubdistrict();
    }
}
