package com.soocompany.wodify.reservation.service;

import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.dto.ReservationCreateReqDto;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public void reservationCreate(ReservationCreateReqDto dto) {
        Reservation reservation = dto.toEntity();
        reservationRepository.save(reservation);
    }
}
