package com.wheredidwego.controller;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.TouristSpot;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.PhotoEntryResponseDto;
import com.wheredidwego.dto.TouristSpotResponse;
import com.wheredidwego.security.details.CustomUserDetails;
import com.wheredidwego.service.TouristSpotService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tourist-spots")
public class TouristSpotController {

    private final TouristSpotService touristSpotService;

    @GetMapping()
    public ResponseEntity<?> getMyPhotoEntries(@RequestParam(value = "swLat", defaultValue = "takenAt")double swLat,
                                               @RequestParam(value = "swLng", defaultValue = "desc")double swLng,
                                               @RequestParam(value = "neLat", defaultValue = "0")double neLat,
                                               @RequestParam(value = "neLng", defaultValue = "10")double neLng,
                                               @RequestParam(value = "level", defaultValue = "5")int level) {

        List<TouristSpot> touristSpots = touristSpotService.getTouristSpotsInBounds(swLat, swLng, neLat, neLng);
        List<TouristSpotResponse> response = touristSpotService.mapToResponseList(touristSpots);

        return ResponseEntity.ok().body(response);
    }
}
