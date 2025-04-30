package com.wheredidwego.service;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.domain.Region;
import com.wheredidwego.domain.User;
import com.wheredidwego.dto.PhotoEntryUploadDto;
import com.wheredidwego.exception.PhotoEntryException;
import com.wheredidwego.repository.PhotoEntryRepository;
import com.wheredidwego.util.awsS3.AwsS3Util;
import com.wheredidwego.util.lib.DateUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class PhotoEntryService {

    private final RegionService regionService;
    private final PhotoEntryRepository photoEntryRepository;
    private final AwsS3Util awsS3Util;

    public PhotoEntry getPhotoEntryById(Long id) {
        return photoEntryRepository.getPhotoEntriesById(id)
                .orElseThrow(() -> new PhotoEntryException("[ERROR]: ID" + id + "의 PhotoEntry가 존재하지 않습니다."));
    }

    /**
     * photo entry 업로드
     * @param dto  업로드할 정보 dto { filename, description, takenAt, lat, lng }
     * @param user 업로드 대상자
     * @return  저장된 Entity (PhotoEntry)
     */
    public PhotoEntry uploadPhotoEntry(PhotoEntryUploadDto dto, User user) {
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

    public List<PhotoEntry> getAllPhotoEntriesByUser(User user) {
        return photoEntryRepository.findAllByUser(user);
    }

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
}
