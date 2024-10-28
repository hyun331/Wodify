package com.soocompany.wodify.wod.dto;

import com.soocompany.wodify.wod.domain.Wod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String boxName;
    private String memberName;
    private LocalDate date;
    private Time timeCap;
    private int rounds;
    private String info;
    private LocalDateTime createdTime;
    private List<WodDetResDto> wodDetResDtoList;

    public static WodResDto fromEntity(Wod wod) {
        return WodResDto.builder()
                .id(wod.getId())
                .boxName(wod.getBox().getName())
                .memberName(wod.getMember().getName())
                .date(wod.getDate())
                .timeCap(wod.getTimeCap())
                .rounds(wod.getRounds())
                .info(wod.getInfo())
                .createdTime(wod.getCreatedTime())
                .wodDetResDtoList(new ArrayList<>())
                .build();
    }
}
