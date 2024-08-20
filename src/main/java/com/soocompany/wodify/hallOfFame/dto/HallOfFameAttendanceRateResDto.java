package com.soocompany.wodify.hallOfFame.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HallOfFameAttendanceRateResDto {
    private String name;
    private String email;
    @Builder.Default
    private int attendanceCount = 0;
}
