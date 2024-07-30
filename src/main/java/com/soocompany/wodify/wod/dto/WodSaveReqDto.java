package com.soocompany.wodify.wod.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
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
}