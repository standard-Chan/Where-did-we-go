package com.wheredidwego.util.awsS3;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Slf4j
@Component
public class S3PresignedUrl {
    @Value("${aws.s3.access-key}")
    private String accessKeyId;
    @Value("${aws.s3.secret-key}")
    private String secretAccessKey;
    @Value("${aws.s3.region}")
    private String region;
    @Value("${aws.s3.bucket}")
    private String bucketName;

    private AwsBasicCredentials awsCreds;

    @PostConstruct
    public void init() {
         this.awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
    }



    public String generateUploadS3PresignedUrl(String path) {


        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(this.awsCreds))
                .build()) {

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10)) // URL 유효 시간
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

            URL url = presignedRequest.url();
            log.info("Generated Presigned URL: {}", url.toString());
            return url.toString();
        }
    }

    public String createPathKey(String userEmail) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return userEmail + "/photoEntry/" + timestamp;
    }

    public String generateDownloadPresignedUrl(String path) {

        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(this.awsCreds))
                .build()) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(5)) // 다운로드 URL 유효시간
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

            URL url = presignedRequest.url();
            return url.toString();
        }
    }

    public void deleteImage(String path) {
        try {
            S3Client s3Client = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                    .build();

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();

            s3Client.deleteObject(deleteRequest);

            log.info("S3 파일 삭제 완료: {}", path);
        } catch (Exception e) {
            log.error("[ERROR] S3 파일 삭제 실패: {}", e.getMessage());
            throw new RuntimeException("S3 파일 삭제 중 오류가 발생했습니다.");
        }
    }
}
