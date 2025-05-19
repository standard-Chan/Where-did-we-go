package com.wheredidwego.service;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.util.awsS3.AwsS3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class S3Service {

    private final AwsS3Util awsS3Util;
    private final PhotoEntryService photoEntryService;

    /**
     * S3에 이미지를 업로드 하는 PresignedUrl 발급 메서드
     * @param userEmail request 대상의 이메일
     * @param filename 저장할 파일 이름
     * @return PresignedUrl
     */
    public String getUploadS3PresignedUrl(String userEmail, String filename) {
        String path = awsS3Util.createImagePath(userEmail, filename);
        return awsS3Util.generateUploadS3PresignedUrl(path);
    }

    /**
     * S3에서 Image를 다운로드하는 Url 발급
     * @param photoS3Path S3에 저장되어 있는 Image path
     * @return 다운로드 presigned url
     */
    public String getDownloadS3PresignedUrl(String photoS3Path) {
        return awsS3Util.generateDownloadPresignedUrl(photoS3Path);
    }

    // 이미지 삭제
    public void deleteImage(String imagePath) {
        awsS3Util.deleteImage(imagePath);
    }
}
