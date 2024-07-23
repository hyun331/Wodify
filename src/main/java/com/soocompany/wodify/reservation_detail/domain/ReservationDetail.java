package com.soocompany.wodify.reservation_detail.domain;

import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.record.domain.Record;
import com.soocompany.wodify.reservation.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

//    @OneToOne(mappedBy = "reservation_detail", cascade = CascadeType.PERSIST) // reservationDetail ?
//    private Record record;

}
