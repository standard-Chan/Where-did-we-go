package com.wheredidwego.service;

import com.wheredidwego.domain.PhotoEntry;
import com.wheredidwego.util.awsS3.S3PresignedUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class S3PresignedUrlService {

    private final S3PresignedUrl s3PresignedUrl;
    private final PhotoEntryService photoEntryService;

    public String getUploadS3PresignedUrl(String userEmail) {
        String path = s3PresignedUrl.createPathKey(userEmail);
        return s3PresignedUrl.generateUploadS3PresignedUrl(path);
    }

    public String getDownloadS3PresignedUrl(Long id) {
        PhotoEntry photoEntry = photoEntryService.getPhotoEntryById(id);

        String photoS3Path = photoEntry.getPhotoPath();
        return s3PresignedUrl.generateDownloadPresignedUrl(photoS3Path);
    }

}
