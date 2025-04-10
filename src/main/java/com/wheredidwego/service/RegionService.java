package com.wheredidwego.service;

import com.wheredidwego.domain.Region;
import com.wheredidwego.repository.RegionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    public Region createRegion(Double lat, Double lng) {
        Region region = new Region(lat, lng);

        return regionRepository.save(region);
    }
}
