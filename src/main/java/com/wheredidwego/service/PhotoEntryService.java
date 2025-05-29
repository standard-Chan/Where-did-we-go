package com.wheredidwego.service;

import com.wheredidwego.domain.Friend;
import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.Region;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.PhotoEntryResponseDto;
import com.wheredidwego.dto.PhotoEntryUpdateRequestDto;
import com.wheredidwego.dto.PhotoEntryUploadRequestDto;
import com.wheredidwego.exception.ErrorCode;
import com.wheredidwego.exception.FriendException;
import com.wheredidwego.exception.PhotoEntryException;
import com.wheredidwego.repository.PhotoEntryRepository;
import com.wheredidwego.util.awsS3.AwsS3Util;
import com.wheredidwego.util.lib.DateUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final S3Service s3Service;
    private final FriendService friendService;

    public PhotoEntry getPhotoEntryById(Long id) {
        return photoEntryRepository.getPhotoEntriesById(id)
                .orElseThrow(() -> new PhotoEntryException("ID-" + id + "의 PhotoEntry가 존재하지 않습니다."));
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
        return photoEntryRepository.findAllInBounds(user, swLat, neLat, swLng, neLng);
    }

    public List<PhotoEntry> getAllPhotoEntriesByUser(User user) {
        return photoEntryRepository.findAllByUser(user);
    }

    // id로 삭제
    public void deletePhotoEntryById(Long id, UserDetails userDetails) {
        PhotoEntry photoEntry = getPhotoEntryById(id);

        // 해당 데이터의 소유 USER와 요청 User가 다른 경우
        if (!photoEntry.getUser().getEmail().equals(userDetails.getUsername())) {
            throw new AuthorizationDeniedException("삭제 권한이 없습니다.");
        }
        // s3 이미지 삭제
        String photoPath = photoEntry.getPhotoPath();
        awsS3Util.deleteImage(photoPath);
        // db 데이터 삭제
        photoEntryRepository.deleteById(id);
    }

    // update
    public PhotoEntry updatePhotoEntry(PhotoEntryUpdateRequestDto requestDto) {
        // photo entry 가져오기
        PhotoEntry photoEntry = this.getPhotoEntryById(requestDto.getId());

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

            // 새로운 region 생성 및 저장
            Region region = new Region(lat, lng, regionService.searchRegion(lat, lng));
            regionService.saveRegion(region);

            // 새로운 region으로 업데이트
            photoEntry.updateRegion(region);
        }

        return photoEntryRepository.save(photoEntry);
    }

    /**
     * photoEntry list를 responseDTO list로 변환
     * @param photoEntries
     * @return
     */
    public List<PhotoEntryResponseDto> wrappingPhotoEntry2Response (List<PhotoEntry> photoEntries) {

        List<PhotoEntryResponseDto> responseDtos;
        long start = System.currentTimeMillis();
        // 데이터가 적을 경우 직렬처리
        if (photoEntries.size() < 100) {

            responseDtos = photoEntries
                    .stream()
                    .map(photoEntry -> {
                        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry);
                        responseDto.setPhotoUrl(s3Service.getDownloadS3PresignedUrl(photoEntry.getPhotoPath()));
                        return responseDto;
                    }).toList();
        } else { // 데이터가 많을 경우 병렬처리
            responseDtos = photoEntries
                    .parallelStream()
                    .map(photoEntry -> {
                        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto(photoEntry);
                        responseDto.setPhotoUrl(s3Service.getDownloadS3PresignedUrl(photoEntry.getPhotoPath()));
                        return responseDto;
                    }).toList();
        }

        long end = System.currentTimeMillis();
        System.out.println("성능시간");
        System.out.println(end - start);
        return responseDtos;
    }

    /**
     * photoEntry list를 Region 정보만 담은 responseDTO list로 변환
     * @param photoEntries
     * @return
     */
    public List<PhotoEntryResponseDto> wrappingPhotoEntry2ResponseOnlyRegion (List<PhotoEntry> photoEntries) {

        List<PhotoEntryResponseDto> responseDtos;
        // 데이터가 적을 경우 직렬처리
        if (photoEntries.size() < 100) {
            responseDtos = photoEntries
                    .stream()
                    .map(photoEntry -> {
                        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto();
                        responseDto.setPhotoEntryResponseDtoForRegion(photoEntry);
                        responseDto.setPhotoUrl("false");
                        return responseDto;
                    }).toList();
        } else { // 데이터가 많을 경우 병렬처리
            responseDtos = photoEntries
                    .parallelStream()
                    .map(photoEntry -> {
                        PhotoEntryResponseDto responseDto = new PhotoEntryResponseDto();
                        responseDto.setPhotoEntryResponseDtoForRegion(photoEntry);
                        responseDto.setPhotoUrl("false");
                        return responseDto;
                    }).toList();
        }

        return responseDtos;
    }


    public List<PhotoEntryResponseDto> getFriendsPhotoEntries(User user, User friendUser, double swLat, double swLng, double neLat, double neLng) {
        List<PhotoEntry> photoEntries;
        List<PhotoEntryResponseDto> photoEntryResponseDtos;
        Friend relationship = friendService.getFriendByUserAndFriend(user, friendUser);
        switch (relationship.getAccessLevel()) {
            case NONE -> {
                throw new FriendException(ErrorCode.NOT_PERMISSION_TO_VIEW);
            }
            case LOCATION_ONLY -> {
                photoEntries = getPhotoEntriesInBounds(friendUser, swLat, swLng, neLat, neLng);
                photoEntryResponseDtos = wrappingPhotoEntry2ResponseOnlyRegion(photoEntries);
            }
            case VIEW_DETAIL -> {
                photoEntries = getPhotoEntriesInBounds(friendUser, swLat, swLng, neLat, neLng);
                photoEntryResponseDtos = wrappingPhotoEntry2Response(photoEntries);
            }
            case FULL_ACCESS -> {
                photoEntries = getPhotoEntriesInBounds(friendUser, swLat, swLng, neLat, neLng);
                photoEntryResponseDtos = wrappingPhotoEntry2Response(photoEntries);
                // 추후 구현
            }
            default -> {
                throw new FriendException(ErrorCode.INCORRECT_PERMISSION);
            }
        }
        return photoEntryResponseDtos;
    }

}
