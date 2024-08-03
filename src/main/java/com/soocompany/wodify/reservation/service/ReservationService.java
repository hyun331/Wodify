package com.soocompany.wodify.reservation.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.repository.BoxRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.domain.Role;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.dto.*;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import com.soocompany.wodify.wod.domain.Wod;
import com.soocompany.wodify.wod.repository.WodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BoxRepository boxRepository;
    private final MemberRepository memberRepository;
    private final WodRepository wodRepository;
    private final ReservationManagementService reservationManagementService;

    public ReservationDetailResDto reservationCreate(ReservationCreateReqDto dto) {
        Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> {
            log.error("reservationCreate() : 해당 id의 회원을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        });
        if (!member.getRole().equals(Role.COACH)) {
            log.error("reservationCreate() : 예약을 생성할 수 있는 권한이 없습니다.");
            throw new IllegalArgumentException("예약을 생성할 수 있는 권한이 없습니다.");
        }
        Box box = boxRepository.findById(dto.getBoxId()).orElseThrow(()-> new EntityNotFoundException("해당하는 id의 박스가 존재하지 않습니다."));
        if (member.getBox()==null || !member.getBox().equals(box)) {
            log.error("reservationCreate() : 박스에 대한 권한이 없습니다.");
            throw new IllegalArgumentException("박스에 대한 권한이 없습니다.");
        }
        if (dto.getDate().isBefore(LocalDate.now())) {
            log.error("reservationCreate() : 오늘 이전에 대한 예약은 생성이 불가합니다.");
            throw new IllegalArgumentException("오늘 이전에 대한 예약은 생성이 불가합니다.");
        }

        Reservation reservation = dto.toEntity(box, member);
        int maximumPeople = dto.getMaximumPeople();
        Reservation savedReservation = reservationRepository.save(reservation);
        reservationManagementService.increaseAvailable(savedReservation.getId(), maximumPeople);
        return savedReservation.detailResDtoFromEntity();
    }

    public ReservationDetailResDto reservationDetail(Long reservationId) {
        Reservation reservation = reservationRepository.findByIdAndDelYn(reservationId,"N").orElseThrow(()-> new EntityNotFoundException("해당 id의 예약이 없습니다."));
        return reservation.detailResDtoFromEntity();
    }
  
    public Page<ReservationListResDto> reservationList(Long boxId, Pageable pageable) {
        Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> {
            log.error("reservationList() : 해당 id의 회원을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        });
        Box box = boxRepository.findById(boxId).orElseThrow(()-> new EntityNotFoundException("해당하는 id의 박스가 존재하지 않습니다."));
        if (member.getBox()==null || !member.getBox().equals(box)) {
            log.error("reservationCreate() : 박스에 대한 권한이 없습니다.");
            throw new IllegalArgumentException("박스에 대한 권한이 없습니다.");
        }
        Page<Reservation> reservationList = reservationRepository.findByBoxAndAndDelYn(box, "N",pageable);
        return reservationList.map(Reservation::ListResDtoFromEntity);
    }

    public Page<ReservationListResDto> reservationListByDate(Long boxId, ReservationListReqDto dto, Pageable pageable){
        Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> {
            log.error("reservationList() : 해당 id의 회원을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        });
        Box box = boxRepository.findById(boxId).orElseThrow(()-> new EntityNotFoundException("해당하는 id의 박스가 존재하지 않습니다."));
        if (member.getBox()==null || !member.getBox().equals(box)) {
            log.error("reservationCreate() : 박스에 대한 권한이 없습니다.");
            throw new IllegalArgumentException("박스에 대한 권한이 없습니다.");
        }
        LocalDate date = dto.getDate();
        return reservationRepository.findByBoxAndDateAndDelYn(box,date,"N",pageable).map(Reservation::ListResDtoFromEntity);
    }

    public ReservationDetailResDto reservationUpdate(Long id, ReservationUpdateReqDto dto) {
        Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> {
            log.error("reservationUpdate() : 해당 id의 회원을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        });
        Reservation reservation = reservationRepository.findByIdAndDelYn(id,"N").orElseThrow(()->new EntityNotFoundException("해당하는 id의 예약이 존재하지 않습다."));
        if (!reservation.getCoach().equals(member)) {
            log.error("reservationUpdate() : 예약 변경에 대한 권한이 없습니다.");
            throw new IllegalArgumentException("예약 변경에 대한 권한이 없습니다.");
        }
        if (dto.getMaximumPeople() < reservation.getMaximumPeople()- reservation.getAvailablePeople()) {
            throw new IllegalArgumentException("현재 예약된 인원보다 적은 인원수로 수정을 불가합니다.");
        }
        reservation.update(dto);
        return reservationRepository.save(reservation).detailResDtoFromEntity();
    }

    public void reservationDelete(Long id) {
        Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> {
            log.error("reservationDelete() : 해당 id의 회원을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        });
        Reservation reservation = reservationRepository.findByIdAndDelYn(id, "N").orElseThrow(() -> new EntityNotFoundException("해당 id의 예약이 없습니다."));
        if (!reservation.getCoach().equals(member)) {
            log.error("reservationDelete() : 예약 변경에 대한 권한이 없습니다.");
            throw new IllegalArgumentException("예약 변경에 대한 권한이 없습니다.");
        }
        reservation.updateDelYn();
        reservationRepository.save(reservation);
    }
}

