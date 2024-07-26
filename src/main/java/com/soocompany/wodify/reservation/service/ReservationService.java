package com.soocompany.wodify.reservation.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.repository.BoxRepository;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.dto.ReservationCreateReqDto;
import com.soocompany.wodify.reservation.dto.ReservationDetailResDto;
import com.soocompany.wodify.reservation.dto.ReservationListReqDto;
import com.soocompany.wodify.reservation.dto.ReservationListResDto;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BoxRepository boxRepository;

    public ReservationDetailResDto reservationCreate(ReservationCreateReqDto dto) {
        Box box = boxRepository.findById(dto.getBoxId()).orElseThrow(()-> new EntityNotFoundException("해당하는 id의 박스가 존재하지 않습니다."));
        Reservation reservation = dto.toEntity(box);
        return reservationRepository.save(reservation).detailResDtoFromEntity();
    }

    public ReservationDetailResDto reservationDetail(Long reservationId) {
        Reservation reservation = reservationRepository.findByIdAndDelYn(reservationId,"N").orElseThrow(()-> new EntityNotFoundException("해당 id의 예약이 없습니다."));
        return reservation.detailResDtoFromEntity();
    }

    public Page<ReservationListResDto> reservationList(Long boxId, Pageable pageable) {
        Box box = boxRepository.findById(boxId).orElseThrow(()-> new EntityNotFoundException("해당하는 id의 박스가 존재하지 않습니다."));
        Page<Reservation> reservationList = reservationRepository.findByBoxAndAndDelYn(box, "N",pageable);
        return reservationList.map(Reservation::ListResDtoFromEntity);
    }

    public Page<ReservationListResDto> reservationListByDate(Long boxId, ReservationListReqDto dto, Pageable pageable){
        Box box = boxRepository.findById(boxId).orElseThrow(()-> new EntityNotFoundException("해당하는 id의 박스가 존재하지 않습니다."));
        LocalDate date = dto.getDate();
//        오늘 이전의 날짜에 대한 목록 조회 불가능
        if (date.isBefore(LocalDate.now())) throw new IllegalArgumentException("예약이 불가능한 접근입니다.");
        return reservationRepository.findByBoxAndAndDateAndAndDelYn(box,date,"N",pageable).map(Reservation::ListResDtoFromEntity);

    }
}
