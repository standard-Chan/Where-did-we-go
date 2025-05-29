package com.wheredidwego.service;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.PhotoEntryResponseDto;
import com.wheredidwego.repository.PhotoEntryRepository;
import com.wheredidwego.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhotoEntrySearchService {

    private final PhotoEntryService photoEntryService;
    private final PhotoEntryRepository photoEntryRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * 정렬시킨 후 Page 단위로 PhotoEntry를 가져오는 메서드
     * @param sort  정렬 기준 칼럼명
     * @param direction asc, desc
     * @param page  조회할 page
     * @param size  한번에 가져올 데이터 수
     * @return Page<PhotoEntryResponseDto>
     */
    public Page<PhotoEntryResponseDto> search(UserDetails userDetails, String sort, String direction, int page, int size) {

        Pageable pageable;
        Page<PhotoEntry> photoEntries;

        User user = userService.findUserByEmail(userDetails.getUsername());

        // 오름차순/내림차순 설정
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        /*
            sort 가 실제로 존재하는 칼럼명인지 확인 및 예외처리
         */


        // 정렬 기준 지정
        if (sort.equals("region")) {
            pageable = PageRequest.of(page, size, Sort.by(sortDirection, "region.province")
                    .and(Sort.by(sortDirection, "region.district"))
                    .and(Sort.by(sortDirection, "region.subdistrict")));
        } else {
            // Pageable 설정
            pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        }

        // get data
        photoEntries = photoEntryRepository.findAllByUser(user, pageable);

        // Dto에 mapping
        return photoEntries.map(photo -> {
            String photoUrl = s3Service.getDownloadS3PresignedUrl(photo.getPhotoPath());
            PhotoEntryResponseDto dto = new PhotoEntryResponseDto(photo, photoUrl);
            return dto;
        });
    }
}
