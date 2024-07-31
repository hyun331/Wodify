package com.soocompany.wodify.wod.dto;

import com.soocompany.wodify.common.BaseEntity;
import com.soocompany.wodify.wod.domain.Wod;
import com.soocompany.wodify.wod.domain.WodDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WodDetResDto {
    
    private Long id;
    private String name;
    private String contents;
    private LocalDateTime createdTime;
    private String delYn;

    public static WodDetResDto fromEntity(WodDetail wodDetail) {
        return WodDetResDto.builder()
                .id(wodDetail.getId())
                .name(wodDetail.getName())
                .contents(wodDetail.getContents())
                .createdTime(wodDetail.getCreatedTime())
                .delYn(wodDetail.getDelYn())
                .build();
    }
}
