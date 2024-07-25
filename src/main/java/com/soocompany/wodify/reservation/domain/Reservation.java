package com.soocompany.wodify.reservation.domain;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.reservation.dto.ReservationDetailResDto;
import com.soocompany.wodify.reservation.dto.ReservationListResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalTime time;

//    @JoinColumn(name = "wod_id")
//    @ManyToOne
//    private Wod wod;

    @ManyToOne
    @JoinColumn(name = "box_id")
    private Box box;

    @JoinColumn(name = "coach_id")
    @ManyToOne
    private Member coach;
    @Column(nullable = false)
    private int maximumPeople;
    @Column(nullable = false)
    private int availablePeople;


    public ReservationListResDto ListResDtoFromEntity() {
        return ReservationListResDto.builder()
                .id(this.id)
                .date(this.date)
                .time(this.time)
                .build();

    }
    public ReservationDetailResDto detailResDtoFromEntity() {
        return ReservationDetailResDto.builder()
                .id(this.id)
                .date(this.date)
                .time(this.time)
                .maximumPeople(this.maximumPeople)
                .availablePeople(this.availablePeople)
                .build();

    }
}
