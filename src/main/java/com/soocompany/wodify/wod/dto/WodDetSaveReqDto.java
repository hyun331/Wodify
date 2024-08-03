package com.soocompany.wodify.wod.dto;

import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.wod.domain.Wod;
import com.soocompany.wodify.wod.domain.WodDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WodDetSaveReqDto extends BaseEntity {
    private String name;
    private String contents;

    public WodDetail toEntity(Wod wod) {
        return WodDetail.builder()
                .wod(wod)
                .name(this.getName())
                .contents(this.getContents())
                .build();
    }
}
