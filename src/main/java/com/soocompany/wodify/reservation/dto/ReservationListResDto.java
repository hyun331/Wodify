package com.soocompany.wodify.reservation.dto;

import com.soocompany.wodify.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationListResDto {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private String coach;
    private Long wod_id;

}