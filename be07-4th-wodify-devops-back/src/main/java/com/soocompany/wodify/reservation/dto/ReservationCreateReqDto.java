package com.soocompany.wodify.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.wod.domain.Wod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateReqDto {
    private Long wodId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "kk:mm")
    private LocalTime time;
    private int maximumPeople;


    public Reservation toEntity(Box box, Member coach, Wod wod) {
        return Reservation.builder()
                .box(box)
                .wod(wod)
                .date(this.date)
                .time(this.time)
                .maximumPeople(this.maximumPeople)
                .availablePeople(this.maximumPeople)
                .coach(coach)
                .build();
    }
}