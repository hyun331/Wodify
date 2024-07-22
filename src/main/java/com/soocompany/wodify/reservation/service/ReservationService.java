package com.soocompany.wodify.reservation.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.repository.BoxRepository;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.dto.ReservationCreateReqDto;
import com.soocompany.wodify.reservation.dto.ReservationDetailResDto;
import com.soocompany.wodify.reservation.dto.ReservationListResDto;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BoxRepository boxRepository;

    public void reservationCreate(ReservationCreateReqDto dto) {
        Box box = boxRepository.findById(dto.getBoxId()).orElseThrow(()-> new EntityNotFoundException("해당하는 id의 박스가 존재하지 않습니다."));
        Reservation reservation = dto.toEntity(box);
        reservationRepository.save(reservation);
    }

    public List<ReservationListResDto> reservationList(Long boxId) {
        Box box = boxRepository.findById(boxId).orElseThrow(()-> new EntityNotFoundException("해당하는 id의 박스가 존재하지 않습니다."));
        List<Reservation> reservationList = reservationRepository.findByBoxAndAndDelYn(box, "N");
        return reservationList.stream().map(Reservation::ListResDtoFromEntity).collect(Collectors.toList());
    }

    public ReservationDetailResDto reservationDetail(Long reservationId) {
        Reservation reservation = reservationRepository.findByIdAndDelYn(reservationId,"N").orElseThrow(()-> new EntityNotFoundException("해당 id의 예약이 없습니다."));
        return reservation.detailResDtoFromEntity();
    }
}
