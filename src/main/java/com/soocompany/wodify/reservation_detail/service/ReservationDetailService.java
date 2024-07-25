package com.soocompany.wodify.reservation_detail.service;

import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.dto.ReservationDetailResDto;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetCreateReqDto;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetailDetResDto;
import com.soocompany.wodify.reservation_detail.repository.ReservationDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ReservationDetailService {
    private final ReservationDetailRepository reservationDetailRepository;
    private final ReservationRepository reservationRepository;
//    private final MemberRepository memberRepository;

    public void create(ReservationDetCreateReqDto dto) {
        Reservation reservation = reservationRepository.findByIdAndDelYn(dto.getReservationId(), "N").orElseThrow(() -> new EntityNotFoundException("해당 id의 예약이 존재하지 않습니다."));
        ReservationDetail reservationDetail = dto.toEntity(reservation);
        reservationDetailRepository.save(reservationDetail);
    }

    public ReservationDetailDetResDto detail(Long id) {
        ReservationDetail reservationDetail = reservationDetailRepository.findByIdAndDelYn(id, "N").orElseThrow(() -> new EntityNotFoundException("해당 id의 예약상세내역을 찾을 수 없습니다."));
        return reservationDetail.detFromEntity();
    }
}