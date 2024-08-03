package com.soocompany.wodify.post.dto;

import com.soocompany.wodify.post.domain.Image;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageResDto {
    private Long id;
    private String fileName;
    private String s3Path;
    private LocalDateTime createdTime;

    public static ImageResDto fromEntity(Image image) {
        return ImageResDto.builder()
                .id(image.getId())
                .fileName(image.getFileName())
                .s3Path(image.getS3Path())
                .createdTime(image.getCreatedTime())
                .build();
    }
}
