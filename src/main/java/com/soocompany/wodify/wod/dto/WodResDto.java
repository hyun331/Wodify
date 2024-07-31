package com.soocompany.wodify.wod.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.common.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.wod.domain.Wod;
import com.soocompany.wodify.wod.domain.WodDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WodResDto {
    private Long id;
    private Long boxId;
    private Long memberId;
    private LocalDate date;
    private Time timeCap;
    private int rounds;
    private String info;
    private LocalDateTime createdTime;
    private List<WodDetResDto> wodDetResDtoList;

    public static WodResDto fromEntity(Wod wod) {
        return WodResDto.builder()
                .id(wod.getId())
                .boxId(wod.getBox().getId())
                .memberId(wod.getMember().getId())
                .date(wod.getDate())
                .timeCap(wod.getTimeCap())
                .rounds(wod.getRounds())
                .info(wod.getInfo())
                .createdTime(wod.getCreatedTime())
                .wodDetResDtoList(new ArrayList<>())
                .build();
    }
}
