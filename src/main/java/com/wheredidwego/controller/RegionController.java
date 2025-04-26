package com.wheredidwego.controller;

import com.wheredidwego.domain.Region;
import com.wheredidwego.dto.RegionInfoDto;
import com.wheredidwego.exception.GeocodingException;
import com.wheredidwego.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/regions")
    public ResponseEntity<?> getRegionById(@RequestParam("id") Long id) {
        try {
            Region region = regionService.findRegionById(id);
            return ResponseEntity.ok().body(region);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/regions")
    public ResponseEntity<?> createRegion(@RequestBody Map<String, String> request) {
        String latStr = request.get("lat");
        String lngStr = request.get("lng");

        try {
            Double lat = Double.parseDouble(latStr);
            Double lng = Double.parseDouble(lngStr);
            Region region = regionService.findOrCreateRegion(lat, lng);
            RegionInfoDto responseDto = new RegionInfoDto(region);
            return ResponseEntity.status(201).body("Region이 등록되었습니다.\n" + responseDto);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("잘못된 형식의 좌표를 입력하였습니다.");
        } catch (GeocodingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/regions/geo")
    public ResponseEntity<?> searchRegion(@RequestParam("lat") double lat, @RequestParam("lng") double lng) {
        try {
            RegionInfoDto regionInfo = regionService.searchRegion(lat, lng);
            return ResponseEntity.ok().body(regionInfo);
        } catch (GeocodingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
