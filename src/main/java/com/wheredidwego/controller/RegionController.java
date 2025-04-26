package com.wheredidwego.controller;

import com.wheredidwego.domain.Region;
import com.wheredidwego.dto.RegionInfoDto;
import com.wheredidwego.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/regions")
    public Region getRegionById(@RequestParam("id") Long id) {
        return regionService.findRegionById(id);
    }

    @PostMapping("/regions")
    public ResponseEntity<?> createRegion(@RequestBody Map<String, String> request) {
        String latStr = request.get("lat");
        String lngStr = request.get("lng");

        try {
            Double lat = Double.parseDouble(latStr);
            Double lng = Double.parseDouble(lngStr);
            Region region = regionService.findOrCreateRegion(lat, lng);
            return ResponseEntity.ok().body("Region이 등록되었습니다." + region);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("잘못된 형식의 좌표를 입력하였습니다.");
        }
    }

    @GetMapping("/regions/geo")
    public ResponseEntity<?> searchRegion(@RequestParam("lat") double lat, @RequestParam("lng") double lng) {
        RegionInfoDto regionInfo = regionService.searchRegion(lat, lng);
        return ResponseEntity.ok().body(regionInfo);
    }
}
