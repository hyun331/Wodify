package com.soocompany.wodify.reservation.dto;

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
public class ReservationDetailResDto {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private Long wod_id;
    private Long coach_id;
    private int maximumPeople;
    private int availablePeople;
}