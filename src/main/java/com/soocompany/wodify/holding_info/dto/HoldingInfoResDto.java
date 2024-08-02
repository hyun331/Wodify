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
public class HoldingInfoResDto {
    private Long id;
    private Long memberId;
    private String memberName;
    private String memberEmail;
    private LocalDate holdingStart;
    private LocalDate holdingEnd;
    private boolean isOnHold;
}
