package com.soocompany.wodify.reservation.domain;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.reservation.dto.ReservationDetailResDto;
import com.soocompany.wodify.reservation.dto.ReservationListResDto;
import com.soocompany.wodify.reservation.dto.ReservationTimeResDto;
import com.soocompany.wodify.reservation.dto.ReservationUpdateReqDto;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetListResDto;
import com.soocompany.wodify.wod.domain.Wod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalTime time;

    @JoinColumn(name = "wod_id")
    @ManyToOne
    private Wod wod;

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

    public void decreaseAvailablePeople() {
        this.availablePeople -= 1;
    }
    public void increaseAvailablePeople() {
        this.availablePeople += 1;
    }
    public ReservationListResDto ListResDtoFromEntity(List<ReservationDetListResDto> dtoList) {
        return ReservationListResDto.builder()
                .id(this.id)
                .date(this.date)
                .time(this.time)
                .maxPeople(this.maximumPeople)
                .reservationPeople(this.maximumPeople - this.availablePeople)
                .coach(this.coach.getName())
                .reservationDetails(dtoList)
                .wod_id(this.wod.getId())
                .build();

    }
    public ReservationDetailResDto detailResDtoFromEntity() {
        return ReservationDetailResDto.builder()
                .id(this.id)
                .date(this.date)
                .time(this.time)
                .wod_id(this.wod.getId())
                .maximumPeople(this.maximumPeople)
                .availablePeople(this.availablePeople)
                .coach_id(this.coach.getId())
                .build();
    }

    public void update(ReservationUpdateReqDto dto) {
        this.date = dto.getDate();
        this.time = dto.getTime();
        this.availablePeople = dto.getMaximumPeople() - this.maximumPeople + this.availablePeople;
        this.maximumPeople = dto.getMaximumPeople();
    }

    public ReservationTimeResDto timeFromEntity() {
        return ReservationTimeResDto.builder().id(this.id).time(this.time).build();
    }
}