package com.wheredidwego.service;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.Region;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.photoEntry.PhotoEntryResponseDto;
import com.wheredidwego.dto.photoEntry.PhotoEntryUpdateRequestDto;
import com.wheredidwego.dto.photoEntry.PhotoEntryUploadRequestDto;
import com.wheredidwego.dto.photoEntry.ProvincePhotoCountResponse;
import com.wheredidwego.enumerate.FriendAccessLevel;
import com.wheredidwego.exception.ErrorCode;
import com.wheredidwego.exception.FriendException;
import com.wheredidwego.exception.PhotoEntryException;
import com.wheredidwego.repository.PhotoEntryRepository;
import com.wheredidwego.util.awsS3.AwsS3Util;
import com.wheredidwego.util.lib.DateUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class PhotoEntryService {

    private final RegionService regionService;
    private final PhotoEntryRepository photoEntryRepository;
    private final AwsS3Util awsS3Util;
    private final FriendService friendService;
    private final PhotoEntryMapper photoEntryMapper;

    public PhotoEntry getPhotoEntryById(Long id) {
        return photoEntryRepository.getPhotoEntriesById(id)
                .orElseThrow(() -> new PhotoEntryException(ErrorCode.PHOTO_ENTRY_NOT_FOUND));
    }

    public PhotoEntry getPhotoEntryById(Long id, User user, User friend) {
        friendService.checkPermission(friend, user);
        return photoEntryRepository.getPhotoEntriesById(id)
                .orElseThrow(() -> new PhotoEntryException(ErrorCode.PHOTO_ENTRY_NOT_FOUND));
    }

    /**
     * photo entry 업로드
     * @param dto  업로드할 정보 dto { filename, description, takenAt, lat, lng }
     * @param user 업로드 대상자
     * @return  저장된 Entity (PhotoEntry)
     */
    public PhotoEntry uploadPhotoEntry(PhotoEntryUploadRequestDto dto, User user) {
        // 좌표(lat,lng)로 Region 생성. 기존 좌표가 존재하면 조회
        Region region = regionService.findOrCreateRegion(dto.getLat(), dto.getLng());
        // 저장된 image의 경로 ({email}/photoEntry/{filename})
        String imagePath = awsS3Util.createImagePath(user.getEmail(), dto.getFilename());

        PhotoEntry entry = PhotoEntry.builder()
                .user(user)
                .region(region)
                .photoPath(imagePath)
                .description(dto.getDescription())
                .takenAt(DateUtil.stringToLocalDate(dto.getTakenAt()))
                .build();

        return photoEntryRepository.save(entry);
    }

    /**
     * 좌표 범위의 사진 데이터를 가져온다
     * @param user 데이터를 얻을 유저
     * @param swLat 남서 위도 좌표
     * @param swLng 남서 경도 좌표
     * @param neLat 북동 위도 좌표
     * @param neLng 북동 경도 좌표
     * @return List photoEntry
     */
    public List<PhotoEntry> getPhotoEntriesInBounds(User user, double swLat, double swLng, double neLat, double neLng) {
        List <PhotoEntry> photoEntries = photoEntryRepository.findAllInBounds(user, swLat, neLat, swLng, neLng);
        return photoEntries;
    }

    public void deletePhotoEntryById(Long id, User user) {
        PhotoEntry photoEntry = getPhotoEntryById(id);

        // 해당 데이터의 소유 USER와 요청 User가 다른 경우
        if (!photoEntry.getUser().getEmail().equals(user.getEmail())) {
            throw new PhotoEntryException(ErrorCode.NOT_PERMISSION_TO_DELETE);
        }

        // s3 이미지 삭제
        String photoPath = photoEntry.getPhotoPath();
        awsS3Util.deleteImage(photoPath);
        // db 데이터 삭제
        photoEntryRepository.deleteById(id);
    }

    /**
     * user가 friend의 photo entity를 삭제하는 메서드
     * @param id photo entry id
     * @param user 삭제 요청 User
     * @param friend 데이터를 삭제할 대상 User
     */
    public void deletePhotoEntryById(Long id, User user, User friend) {
        PhotoEntry photoEntry = getPhotoEntryById(id);
        FriendAccessLevel accessLevel = friendService.getAccessLevel(friend, user);

        // 삭제 권한이 없는 경우
        if (!accessLevel.equals(FriendAccessLevel.FULL_ACCESS)) {
            throw new PhotoEntryException(ErrorCode.NOT_PERMISSION_TO_DELETE);
        }

        // s3 이미지 삭제
        String photoPath = photoEntry.getPhotoPath();
        awsS3Util.deleteImage(photoPath);
        // db 데이터 삭제
        photoEntryRepository.deleteById(id);
    }

    // update
    public PhotoEntry updatePhotoEntry(Long id, PhotoEntryUpdateRequestDto requestDto) {
        // photo entry 가져오기
        PhotoEntry photoEntry = this.getPhotoEntryById(id);

        // takenAt 업데이트
        if (!DateUtil.localDateToString(photoEntry.getTakenAt()).equals(requestDto.getTakenAt())) {
            LocalDate takenAt = DateUtil.stringToLocalDate(requestDto.getTakenAt());
            photoEntry.updateTakenAt(takenAt);
        }

        // description 업데이트
        if (!photoEntry.getDescription().equals(requestDto.getDescription())){
            photoEntry.updateDescription(requestDto.getDescription());
        }

        // lat, lng 업데이트
        if ((photoEntry.getRegion().getLat() != requestDto.getLat())
                || photoEntry.getRegion().getLng() != requestDto.getLng()) {
            double lat = requestDto.getLat();
            double lng = requestDto.getLng();

            // 기존 region 참조 횟수 감소
            regionService.disconnectRegion(photoEntry.getRegion().getId());

            // 새로운 region 생성 or 저장
            Region region = regionService.findOrCreateRegion(lat, lng);

            // 새로운 region으로 업데이트
            photoEntry.updateRegion(region);
        }

        return photoEntryRepository.save(photoEntry);
    }


    /**
     * 권한에 따른 친구의 사진 저장 정보를 반환
     */
    public List<PhotoEntryResponseDto> getFriendsPhotoEntries(User user, User friendUser, double swLat, double swLng, double neLat, double neLng) {
        List<PhotoEntry> photoEntries;
        List<PhotoEntryResponseDto> photoEntryResponseDtos;

        FriendAccessLevel accessLevel = friendService.getAccessLevel(friendUser, user);

        switch (accessLevel) {
            case NONE -> throw new FriendException(ErrorCode.NOT_PERMISSION_TO_VIEW);
            case LOCATION_ONLY -> { // 좌표 정보 관람 가능
                photoEntries = getPhotoEntriesInBounds(friendUser, swLat, swLng, neLat, neLng);
                photoEntryResponseDtos = photoEntryMapper.mapToDtoListOnlyRegion(photoEntries);
            }
            case VIEW_DETAIL, FULL_ACCESS -> {
                photoEntries = getPhotoEntriesInBounds(friendUser, swLat, swLng, neLat, neLng);
                photoEntryResponseDtos = photoEntryMapper.mapToDtoList(photoEntries);
            }
            default -> throw new FriendException(ErrorCode.INCORRECT_PERMISSION);
        }
        return photoEntryResponseDtos;
    }

    /**
     * user의 photo entry의 province별 집계 List 반환
     */
    public List<ProvincePhotoCountResponse> getPhotoEntryStatisticsByProvince(User user) {
        return photoEntryRepository.findPhotoEntriesCountsPerProvince(user);
    }

//    /** native query로 subquery를 from 절에 넣고 join하여 데이터를 가져오기
//     */
//    public List<PhotoEntryWithRegionDto> getPhotoEntryWithRegionDto(User user, double swLat, double swLng, double neLat, double neLng) {
//        long start = System.currentTimeMillis();
//        List<PhotoEntryWithRegionDto> result = photoEntryRepository.findAllInBoundsNative(user.getId(), swLat, neLat, swLng, neLng)
//                .stream()
//                .map(r -> new PhotoEntryWithRegionDto(
//                        ((Number) r[0]).longValue(),
//                        (String) r[1],
//                        (String) r[2],
//                        r[3] != null ? ((java.sql.Date) r[3]).toLocalDate() : null,
//                        (Double) r[4],
//                        (Double) r[5],
//                        (String) r[6],
//                        (String) r[7],
//                        (String) r[8]
//                )).toList();
//
//        long end = System.currentTimeMillis();
//        System.out.println("소요시간(native) : " + (end-start));
//
//        return result;
//    }
}
