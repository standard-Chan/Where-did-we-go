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

    public Region createRegion(Double lat, Double lng) {
        RegionInfoDto regionInfo;
        try {
            regionInfo = reverseGeocodingService.getRegionFromCoords(lat, lng);
        } catch (GeocodingException e) {
            regionInfo = new RegionInfoDto("UNKNOWN", "UNKNOWN");
        }

        Region region = new Region(lat, lng, regionInfo);
        return regionRepository.save(region);
    }

    public Region findRegionById(Long id) {
        return regionRepository.findRegionById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Region id입니다."));
    }

}
