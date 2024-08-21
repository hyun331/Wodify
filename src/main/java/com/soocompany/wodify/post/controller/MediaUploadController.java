package com.soocompany.wodify.post.controller;
import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.post.dto.*;
import com.soocompany.wodify.post.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MediaUploadController {
    private final ImageService imageService;

    // 확장자와 MediaType 매핑 설정
    private static final Map<String, MediaType> MEDIA_TYPE_MAP = new HashMap<>();
    static {
        MEDIA_TYPE_MAP.put("png", MediaType.IMAGE_PNG);
        MEDIA_TYPE_MAP.put("jpg", MediaType.IMAGE_JPEG);
        MEDIA_TYPE_MAP.put("jpeg", MediaType.IMAGE_JPEG);
        MEDIA_TYPE_MAP.put("gif", MediaType.IMAGE_GIF);
        MEDIA_TYPE_MAP.put("mp4", MediaType.valueOf("video/mp4"));
        MEDIA_TYPE_MAP.put("webm", MediaType.valueOf("video/webm"));
        MEDIA_TYPE_MAP.put("ogg", MediaType.valueOf("video/ogg"));
        MEDIA_TYPE_MAP.put("mp3", MediaType.valueOf("audio/mpeg"));
        // 필요한 확장자에 대한 MediaType을 추가하세요.
    }

    @PostMapping("/upload-media")
    public ResponseEntity<?> uploadMedia(@ModelAttribute ImageSaveReqDto dto) throws IOException {
        MultipartFile file = dto.getFile();
        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            return new ResponseEntity<>("Invalid file name", HttpStatus.BAD_REQUEST);
        }

        // 파일 확장자를 추출
        String fileExtension = getFileExtension(fileName).toLowerCase();

        // 확장자에 따른 MediaType 설정
        MediaType mediaType = MEDIA_TYPE_MAP.getOrDefault(fileExtension, MediaType.APPLICATION_OCTET_STREAM);

        // 파일 업로드 로직
        String url = imageService.uploadMedia(file);

        // 응답 데이터 구성
        HttpStatus code = HttpStatus.OK;
        String msg = "파일이 S3에 업로드 되었습니다.";
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("url", url);
        responseData.put("type", mediaType.toString()); // MediaType 정보를 함께 반환

        CommonResDto commonResDto = new CommonResDto(code, msg, responseData);

        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }



    // 파일 확장자를 추출하는 메서드
    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return (lastIndex == -1) ? "" : fileName.substring(lastIndex + 1);
    }
}