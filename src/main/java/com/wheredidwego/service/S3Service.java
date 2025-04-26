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

    public String getUploadS3PresignedUrl(String userEmail) {
        String path = awsS3Util.createPathKey(userEmail);
        return awsS3Util.generateUploadS3PresignedUrl(path);
    }

    public String getDownloadS3PresignedUrl(Long id) {
        PhotoEntry photoEntry = photoEntryService.getPhotoEntryById(id);

        String photoS3Path = photoEntry.getPhotoPath();
        return awsS3Util.generateDownloadPresignedUrl(photoS3Path);
    }

    public void deleteImage(String imagePath) {
        awsS3Util.deleteImage(imagePath);
    }
}
