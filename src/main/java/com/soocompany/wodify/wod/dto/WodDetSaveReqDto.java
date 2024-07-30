package com.soocompany.wodify.wod.dto;

import com.soocompany.wodify.common.BaseEntity;
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
}
