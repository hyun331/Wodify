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
    private String url;
    private LocalDateTime createdTime;

    public static ImageResDto fromEntity(Image image) {
        return ImageResDto.builder()
                .id(image.getId())
                .url(image.getUrl())
                .createdTime(image.getCreatedTime())
                .build();
    }
}
