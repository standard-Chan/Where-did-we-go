package com.wheredidwego.service;

import com.wheredidwego.util.aws.S3PresignedUrl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;



@Service
@RequiredArgsConstructor
public class S3PresignedUrlService {

    private final S3PresignedUrl s3PresignedUrl;

    public String getUploadS3PresignedUrl(String userEmail) {
        String path = s3PresignedUrl.createPathKey(userEmail);
        return s3PresignedUrl.generateUploadS3PresignedUrl(path);
    }

    public String getDownloadS3PresignedUrl(String photoEntryId) {

    }

}
