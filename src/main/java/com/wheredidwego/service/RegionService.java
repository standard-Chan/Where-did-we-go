package com.wheredidwego.service;

import com.wheredidwego.domain.Region;
import com.wheredidwego.dto.RegionInfoDto;
import com.wheredidwego.exception.GeocodingException;
import com.wheredidwego.repository.RegionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final KakaoReverseGeocodingService reverseGeocodingService;

    /**
     * Region 생성.
     * 기존 좌표의 Region이 있을 경우, 생성하지 않고 기존 Region 반환
     * @param lat
     * @param lng
     * @return 생성/검색된 Region
     */
    public Region findOrCreateRegion(Double lat, Double lng) {

        // 중복된 Region -> 기존 Region 반환
        if (regionRepository.existsRegionByLatAndLng(lat, lng)) {
            Region region = regionRepository.findRegionByLatAndLng(lat, lng).get();
            region.increaseReferenceCount();
            return region;
        }

        // 새로운 Region 생성
        RegionInfoDto regionInfo = searchRegion(lat, lng);
        Region newRegion = new Region(lat, lng, regionInfo);
        return regionRepository.save(newRegion);
    }

    public Region findRegionById(Long id) {
        return regionRepository.findRegionById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Region id입니다."));
    }

    public RegionInfoDto searchRegion(Double lat, Double lng) {
        RegionInfoDto regionInfo;
        try {
            regionInfo = reverseGeocodingService.getRegionFromCoords(lat, lng);
        } catch (GeocodingException e) {
            throw new GeocodingException("e.getMessage()" + "(" + lat + "," + lng + ")");
        }
        return regionInfo;
    }
}
