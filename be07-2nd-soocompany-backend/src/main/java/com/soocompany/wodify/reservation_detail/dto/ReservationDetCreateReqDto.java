package com.soocompany.wodify.reservation_detail.dto;

import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetCreateReqDto {
    private Long reservationId;

    public ReservationDetail toEntity(Reservation reservation, Member member) {
        return ReservationDetail.builder()
                .reservation(reservation)
                .member(member)
                .build();

    }
}
