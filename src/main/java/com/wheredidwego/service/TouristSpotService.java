package com.wheredidwego.service;

import com.wheredidwego.domain.TouristSpot;
import com.wheredidwego.dto.TouristSpotResponse;
import com.wheredidwego.repository.TouristSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TouristSpotService {

    private final TouristSpotRepository touristSpotRepository;

    /**
     * 좌표 범위 내의 tourist spots(관광지) 데이터를 반환
     */
    public List<TouristSpot> getTouristSpotsInBounds(double swLat, double swLng, double neLat, double neLng) {
        return touristSpotRepository.findAllInBounds(swLat, neLat, swLng, neLng);
    }

    /**
     * Entity 를 response dto 로 변환
     */
    public List<TouristSpotResponse> mapToResponseList (List<TouristSpot> touristSpots) {
        return touristSpots.parallelStream().map(TouristSpotResponse::new).toList();
    }
}
