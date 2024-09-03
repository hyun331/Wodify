package com.soocompany.wodify.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationTimeResDto {
    private Long id;
    private LocalTime time;
    private int maxPeople;
    private int reservationPeople;
}
