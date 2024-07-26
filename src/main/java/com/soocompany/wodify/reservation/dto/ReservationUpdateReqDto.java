package com.soocompany.wodify.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.reservation.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUpdateReqDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "kk:mm:ss")
    private LocalTime time;
    private int maximumPeople;
    private String coachEmail;

}
