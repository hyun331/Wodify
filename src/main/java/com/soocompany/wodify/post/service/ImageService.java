package com.soocompany.wodify.post.service;

import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.post.domain.Image;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Client s3Client;
    private final ImageRepository imageRepository;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<Image> uploadImages(Post post, MultipartFile[] files) throws IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            byte[] bytes = file.getBytes();
            String fileName = post.getId() + "_" + file.getOriginalFilename();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
            String s3Path = s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(fileName)).toExternalForm();

            Image image = Image.builder()
                    .post(post)
                    .url(s3Path)
                    .build();

            imageRepository.save(image);
            images.add(image);
        }
        return images;
    }

    public void imageDelete(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> {
            log.error("imageDelete() : 해당 id의 image 를 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 image 를 찾을 수 없습니다.");
        });
        if (image.getDelYn().equals("Y")) {
            log.error("imageDelete() : 삭제된 image 입니다.");
            throw new IllegalArgumentException("삭제된 image 입니다.");
        }
        image.updateDelYn();
    }
}
