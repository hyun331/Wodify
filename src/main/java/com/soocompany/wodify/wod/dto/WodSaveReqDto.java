package com.soocompany.wodify.wod.dto;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.wod.domain.Wod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WodSaveReqDto {
    private LocalDate date;
    private Time timeCap;
    private int rounds;
    private String info;
    private List<WodDetSaveReqDto> wodDetSaveReqDtoList;

    public Wod toEntity(Member member, Box box) {
        return Wod.builder()
                .box(box)
                .member(member)
                .date(this.getDate())
                .timeCap(this.getTimeCap())
                .rounds(this.getRounds())
                .info(this.getInfo())
                .wodDetails(new ArrayList<>())
                .build();
    }
}