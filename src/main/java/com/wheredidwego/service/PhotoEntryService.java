package com.wheredidwego.service;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.Region;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.PhotoEntryUploadDto;
import com.wheredidwego.repository.PhotoEntryRepository;
import com.wheredidwego.util.lib.Date;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class PhotoEntryService {

    private final RegionService regionService;
    private final PhotoEntryRepository photoEntryRepository;

    public PhotoEntry uploadPhotoEntry(PhotoEntryUploadDto dto, User user) {
        // 좌표 기반 Region 생성 or 조회
        System.out.println("uploadPhotoEntry---------------------------------");
        Region region = regionService.findOrCreateRegion(dto.getLat(), dto.getLng());

        PhotoEntry entry = PhotoEntry.builder()
                .user(user)
                .region(region)
                .photoUrl(dto.getPhotoUrl())
                .description(dto.getDescription())
                .takenAt(Date.stringToLocalDate(dto.getTakenAt()))
                .build();

        return photoEntryRepository.save(entry);
    }
}
