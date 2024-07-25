package com.soocompany.wodify.reservation_detail.domain;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetailDetResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    public ReservationDetailDetResDto detFromEntity() {
        Box box = this.reservation.getBox();
        return ReservationDetailDetResDto.builder()
                .boxId(box.getId())
                .BoxName(box.getName())
                .memberId(this.member.getId())
                .memberName(this.member.getName())
                .date(reservation.getDate())
                .time(reservation.getTime())
                .build();
    }
}
