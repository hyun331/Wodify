package com.soocompany.wodify.post.controller;
import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.post.dto.*;
import com.soocompany.wodify.post.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    private static final Map<String, String> SIMPLE_TYPE_MAP = new HashMap<>();
    static {
        SIMPLE_TYPE_MAP.put("png", "image");
        SIMPLE_TYPE_MAP.put("jpg", "image");
        SIMPLE_TYPE_MAP.put("jpeg", "image");
        SIMPLE_TYPE_MAP.put("gif", "image");
        SIMPLE_TYPE_MAP.put("mp4", "video");
        SIMPLE_TYPE_MAP.put("webm", "video");
        SIMPLE_TYPE_MAP.put("ogg", "video");
    }

    @PostMapping("/upload-media")
    public ResponseEntity<?> uploadMedia(@ModelAttribute ImageSaveReqDto dto) throws IOException {
        MultipartFile file = dto.getFile();
        String fileName = file.getOriginalFilename();
        if (fileName == null) { return new ResponseEntity<>("Invalid file name", HttpStatus.BAD_REQUEST); }

        String type = getType(fileName);
        String url = imageService.uploadMedia(file);

        HttpStatus code = HttpStatus.OK;
        String msg = "파일이 S3에 업로드 되었습니다.";
        Map<String, String> responseData = new HashMap<>();
        responseData.put("type", type);
        responseData.put("url", url);

        CommonResDto commonResDto = new CommonResDto(code, msg, responseData);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    private String getType(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex == -1) { return "unknown"; }
        String fileExtension = fileName.substring(lastIndex + 1).toLowerCase();
        return SIMPLE_TYPE_MAP.getOrDefault(fileExtension, "unknown");
    }
}