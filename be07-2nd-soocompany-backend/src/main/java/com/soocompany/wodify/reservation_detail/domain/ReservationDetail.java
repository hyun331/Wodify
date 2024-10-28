package com.soocompany.wodify.reservation_detail.domain;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.record.domain.Record;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetListResDto;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetailDetResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "record_id")
    private Record record;

    public ReservationDetailDetResDto detFromEntity() {

        return ReservationDetailDetResDto.builder()
                .id(this.id)
                .memberName(this.member.getName())
                .date(String.valueOf(reservation.getDate()))
                .time(String.valueOf(reservation.getTime()))
                .coachName(reservation.getCoach().getName())
                .wodId(reservation.getWod().getId())
                .recordId(this.record != null && "N".equals(this.record.getDelYn()) ? this.record.getId() : null)
                .recordSnF(this.record != null && "N".equals(this.record.getDelYn()) ? this.record.getSnf() : null)
                .createdTime(String.valueOf(this.getCreatedTime()))
                .build();
    }

    public ReservationDetListResDto listDetFromEntity(){
        return ReservationDetListResDto.builder()
                .id(this.id)
                .memberName(member.getName())
                .build();
    }

    public void updateRecord(Record record) {
        this.record = record;
    }
}