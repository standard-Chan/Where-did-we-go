package com.wheredidwego.controller;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.dto.PhotoEntryResponseDto;
import com.wheredidwego.service.PhotoEntrySearchService;
import com.wheredidwego.service.PhotoEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final PhotoEntryService photoEntryService;
    private final PhotoEntrySearchService photoEntrySearchService;

    @GetMapping("/photo-entries")
    public ResponseEntity<?> searchPhotoEntries(@RequestParam(value = "sort", defaultValue = "takenAt")String sort,
                                                @RequestParam(value = "direction", defaultValue = "desc")String direction,
                                                @RequestParam(value = "page", defaultValue = "0")int page,
                                                @RequestParam(value = "size", defaultValue = "10")int size) {

        Page<PhotoEntryResponseDto> response = photoEntrySearchService.search(sort, direction, page, size);
        return ResponseEntity.ok(response);
    }

}
