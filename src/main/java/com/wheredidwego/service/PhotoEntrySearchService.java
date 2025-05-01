package com.wheredidwego.service;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.dto.PhotoEntryResponseDto;
import com.wheredidwego.repository.PhotoEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhotoEntrySearchService {

    private final PhotoEntryService photoEntryService;
    private final PhotoEntryRepository photoEntryRepository;
    private final S3Service s3Service;

    /**
     * 정렬시킨 후 Page 단위로 PhotoEntry를 가져오는 메서드
     * @param sort  정렬 기준 칼럼명
     * @param direction asc, desc
     * @param page  조회할 page
     * @param size  한번에 가져올 데이터 수
     * @return Page<PhotoEntryResponseDto>
     */
    public Page<PhotoEntryResponseDto> search(String sort, String direction, int page, int size) {
        // 정렬 기준 및 차순 설정
        Sort sortBy;
        if (direction.equals("asc"))
            sortBy = Sort.by(sort).ascending();
        else sortBy = Sort.by(sort).descending();

        /*
            sort 가 실제로 존재하는 칼럼명인지 확인 및 예외처리
         */

        // Pageable 설정
        Pageable pageable = PageRequest.of(page, size, sortBy);

        Page<PhotoEntry> photoEntries = photoEntryRepository.findAll(pageable);

        // Dto에 mapping
        return photoEntries.map(photo -> {
            PhotoEntryResponseDto dto = new PhotoEntryResponseDto(photo);
            dto.setPhotoUrl(s3Service.getDownloadS3PresignedUrl(photo.getId()));
            return dto;
        });
    }
}
