package com.soocompany.wodify.reservation_detail.service;

import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.dto.ReservationDetailResDto;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetCreateReqDto;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetailDetResDto;
import com.soocompany.wodify.reservation_detail.repository.ReservationDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ReservationDetailService {
    private final ReservationDetailRepository reservationDetailRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReservationDetailResDto create(ReservationDetCreateReqDto dto) {
        Reservation reservation = reservationRepository.findByIdAndDelYn(dto.getReservationId(), "N").orElseThrow(() -> new EntityNotFoundException("해당 id의 예약이 존재하지 않습니다."));
        Member member = memberRepository.findByEmailAndDelYn(dto.getMemberEmail(), "N").orElseThrow(()-> new EntityNotFoundException("해당 id의 회원이 존재하지 않습니다."));
        ReservationDetail reservationDetail = dto.toEntity(reservation, member);
        reservationDetailRepository.save(reservationDetail);

        if (reservation.getAvailablePeople() > 0) {
            reservation.decreaseAvailablePeople();
           return reservationRepository.save(reservation).detailResDtoFromEntity();// DB 업데이트
        }else {
            throw new IllegalStateException("예약 인원이 초과되어 예약이 불가능합니다.");
        }
    }

    public ReservationDetailDetResDto detail(Long id) {
        ReservationDetail reservationDetail = reservationDetailRepository.findByIdAndDelYn(id, "N").orElseThrow(() -> new EntityNotFoundException("해당 id의 예약상세내역을 찾을 수 없습니다."));
        return reservationDetail.detFromEntity();
    }
}
