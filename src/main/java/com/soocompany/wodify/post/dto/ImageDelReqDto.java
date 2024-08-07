package com.soocompany.wodify.post.dto;
import com.soocompany.wodify.post.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDelReqDto {
    private Long id;
}
