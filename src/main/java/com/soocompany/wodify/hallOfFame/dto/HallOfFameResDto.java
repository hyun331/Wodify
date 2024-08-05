package com.soocompany.wodify.hallOfFame.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HallOfFameResDto {
    private Long id;
    private int rank;
    private String name;
    private BigDecimal deadLift;
    private BigDecimal squat;
    private BigDecimal benchPress;
    private BigDecimal total;
}
