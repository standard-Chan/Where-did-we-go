package com.wheredidwego.controller;

import com.wheredidwego.domain.Region;
import com.wheredidwego.dto.RegionInfoDto;
import com.wheredidwego.dto.RegionRequest;
import com.wheredidwego.dto.RegionResponse;
import com.wheredidwego.exception.GeocodingException;
import com.wheredidwego.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/{id}")
    public ResponseEntity<RegionResponse> getRegionById(@PathVariable("id") Long id) {
            Region region = regionService.findRegionById(id);
            RegionResponse response = new RegionResponse(region);
            return ResponseEntity.ok().body(response);
    }

    @PostMapping()
    public ResponseEntity<RegionResponse> createRegion(@RequestBody RegionRequest regionRequest) {

        Region region = regionService.findOrCreateRegion(regionRequest.getLat(), regionRequest.getLng());

        RegionResponse response = new RegionResponse(region);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/geo")
    public ResponseEntity<RegionInfoDto> searchRegion(@RequestParam("lat") double lat, @RequestParam("lng") double lng) {
        RegionInfoDto regionInfo = regionService.searchRegion(lat, lng);
        return ResponseEntity.ok().body(regionInfo);
    }
}
