package com.soocompany.wodify.hallOfFame.dto;

import com.soocompany.wodify.wod.domain.Wod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
public class HallOfFameListResDto {
    private List<HallOfFameResDto> hallOfFameResDtoList;
    private Long wodId;

    public HallOfFameListResDto(List<HallOfFameResDto> hallOfFameResDtoList, Long wodId){
        this.hallOfFameResDtoList = hallOfFameResDtoList;
        this.wodId = wodId;
    }
}
