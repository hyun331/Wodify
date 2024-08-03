package com.soocompany.wodify.holding_info.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoldingInfoListResDto {
    private Long id;
    private LocalDate holdingStart;
    private LocalDate holdingEnd;
}
