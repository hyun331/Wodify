package com.soocompany.wodify.hallOfFame.dto;

import com.soocompany.wodify.wod.domain.Wod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@Builder
public class HallOfFameListResDto {
    private List<HallOfFameResDto> hallOfFameResDtoList;
    private Long wodId;

    public HallOfFameListResDto(List<HallOfFameResDto> hallOfFameResDtoList, Long wodId){
        this.hallOfFameResDtoList = hallOfFameResDtoList;
        this.wodId = wodId;
    }
}
