package com.wheredidwego.service;

import com.wheredidwego.domain.Region;
import com.wheredidwego.dto.RegionInfoDto;
import com.wheredidwego.dto.RegionRequest;
import com.wheredidwego.exception.ErrorCode;
import com.wheredidwego.exception.GeocodingException;
import com.wheredidwego.exception.RegionException;
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
     * 기존 좌표의 Region이 있을 경우, 생성하지 않고 기존 Region 반환.
     * 없을 경우 생성하여 Region 반환.
     * @return 생성/검색된 Region
     */
    public Region findOrCreateRegion(double lat, double lng) {

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

    /**
     * Region 참조 횟수 감소
     * reference count에 -1 을 하되, 0이 되면 해당 Region 삭제
     * @param id Region id
     */
    public void disconnectRegion(Long id) {
        Region region = this.findRegionById(id);
        region.decreaseReferenceCount();

        // 참조 횟수가 0인경우 삭제
        if (region.getReferenceCount() <= 0) {
            regionRepository.deleteById(id);
        }
        else {
            regionRepository.save(region);
        }
    }

    public Region findRegionById(Long id) {
        return regionRepository.findRegionById(id)
                .orElseThrow(() -> new RegionException(ErrorCode.REGION_NOT_FOUND));
    }

    /**
     * 좌표로 Region을 검색하는 메서드
     * @param lat 위도
     * @param lng 경도
     * @return  RegionInfoDto (도, 행정구역, 읍면동)
     */
    public RegionInfoDto searchRegion(Double lat, Double lng) {
        RegionInfoDto regionInfo;
        regionInfo = reverseGeocodingService.getRegionFromCoords(lat, lng);
        return regionInfo;
    }

}
