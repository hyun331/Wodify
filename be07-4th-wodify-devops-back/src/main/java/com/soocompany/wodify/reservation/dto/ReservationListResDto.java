package com.soocompany.wodify.reservation.dto;

import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetListResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationListResDto {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private int maxPeople;
    private int reservationPeople;
    private String coach;
    private Long wod_id;
    private List<ReservationDetListResDto> reservationDetails;

}