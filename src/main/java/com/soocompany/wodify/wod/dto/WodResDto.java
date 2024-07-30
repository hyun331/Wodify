package com.soocompany.wodify.wod.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.common.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.wod.domain.WodDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WodResDto extends BaseEntity {
    private Long id;
    private LocalDate date;
    private Time timeCap;
    private int rounds;
    private String info;
    @Builder.Default
    private List<WodDetResDto> wodDetResDtoList = new ArrayList<>();
}
