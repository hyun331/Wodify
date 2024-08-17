package com.soocompany.wodify.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadMedia(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + "_" + UUID.randomUUID().toString() + extension;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder() .bucket(bucket) .key(fileName) .build();
        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(fileName)).toExternalForm();
    }
}
