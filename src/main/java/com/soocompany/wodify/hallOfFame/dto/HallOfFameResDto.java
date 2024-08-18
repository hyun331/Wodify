package com.soocompany.wodify.hallOfFame.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HallOfFameResDto {
    private int rank;
    private String name;
    private String email;
    private LocalTime exerciseTime;

}
