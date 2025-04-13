package com.wheredidwego.service;

import com.wheredidwego.domain.Region;
import com.wheredidwego.dto.RegionInfoDto;
import com.wheredidwego.exception.GeocodingException;
import com.wheredidwego.repository.RegionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final KakaoReverseGeocodingService reverseGeocodingService;

    public Region createRegion(Double lat, Double lng) {
        Optional<Region> optionalRegion = regionRepository.findRegionByLatAndLng(lat, lng);

        // 중복된 Region
        if (optionalRegion.isPresent()) {
            Region region = optionalRegion.get();
            region.increaseReferenceCount();
            return region;
        }

        // 새로운 Region
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
            regionInfo = new RegionInfoDto("UNKNOWN", "UNKNOWN", "UNKNOWN");
        }
        return regionInfo;
    }
}
