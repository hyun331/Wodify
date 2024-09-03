package com.soocompany.wodify.reservation.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.repository.BoxRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.domain.Role;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.dto.*;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetListResDto;
import com.soocompany.wodify.reservation_detail.repository.ReservationDetailRepository;
import com.soocompany.wodify.wod.domain.Wod;
import com.soocompany.wodify.wod.repository.WodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private final ReservationDetailRepository reservationDetailRepository;

    public List<ReservationDetailResDto> reservationCreate(List<ReservationCreateReqDto> dtos) {
        Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> {
            log.error("reservationCreate() : 해당 id의 회원을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        });
        Box box = boxRepository.findByIdAndDelYn(member.getBox().getId(),"N").orElseThrow(()-> new EntityNotFoundException("해당하는 id의 박스가 존재하지 않습니다."));

        List<ReservationDetailResDto> list = new ArrayList<>();
        for (ReservationCreateReqDto dto : dtos) {
            Wod wod = wodRepository.findByBoxIdAndDateAndDelYn(box.getId(), dto.getDate(), "N").orElseThrow(() -> {
                log.error("reservationCreate() : 해당 와드를 찾을 수 없습니다.");
                throw  new EntityNotFoundException("해당 와드를 찾을 수 없습니다.");
            });
            boolean isDuplicate = reservationRepository.existsByBoxAndDateAndTimeAndDelYn(
                    box, dto.getDate(), dto.getTime(), "N"
            );

            if (isDuplicate) {
                log.error("reservationCreate() : 이미 동일한 시간에 예약이 존재합니다.");
                throw new IllegalStateException("이미 동일한 시간에 예약이 존재합니다.");
            }

            Reservation reservation = dto.toEntity(box, member, wod);
            int maximumPeople = dto.getMaximumPeople();
            Reservation savedReservation = reservationRepository.save(reservation);
            reservationManagementService.increaseAvailable(savedReservation.getId(), maximumPeople);
            ReservationDetailResDto detailResDto = savedReservation.detailResDtoFromEntity();
            list.add(detailResDto);
        }


        return list;
    }

    public ReservationDetailResDto reservationDetail(Long reservationId) {
        Reservation reservation = reservationRepository.findByIdAndDelYn(reservationId,"N").orElseThrow(()-> new EntityNotFoundException("해당 id의 예약이 없습니다."));
        return reservation.detailResDtoFromEntity();
    }

    public Page<ReservationListResDto> reservationList(ReservationSearchDto searchDto, Pageable pageable) {
        Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> {
            log.error("reservationList() : 해당 id의 회원을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        });
        Box box = boxRepository.findById(member.getBox().getId()).orElseThrow(() -> new EntityNotFoundException("해당하는 id의 박스가 존재하지 않습니다."));
        if (member.getBox() == null || !member.getBox().equals(box)) {
            log.error("reservationCreate() : 박스에 대한 권한이 없습니다.");
            throw new IllegalArgumentException("박스에 대한 권한이 없습니다.");
        }
        Page<Reservation> reservationList;
        Sort sort = Sort.by(Sort.Order.desc("date"), Sort.Order.desc("time")); // 날짜 및 시간으로 내림차순 정렬

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        if (searchDto.getStartDate()!=null&&searchDto.getEndDate()!=null) {
            reservationList = reservationRepository.findByBoxAndDateBetweenAndDelYn(box, searchDto.getStartDate(), searchDto.getEndDate(), "N", sortedPageable);
        }else {
            reservationList = reservationRepository.findByBoxAndDelYn(box,"N", sortedPageable);
        }
        List<ReservationListResDto> list = new ArrayList<>();

        //  Reservation to ReservationListResDto 변환
        for (Reservation reservation : reservationList) {
            List<ReservationDetListResDto> dtoList = new ArrayList<>();
            List<ReservationDetail> reservationDetails = reservationDetailRepository.findByReservationAndDelYn(reservation, "N");
            for (ReservationDetail reservationDetail : reservationDetails) {
                dtoList.add(reservationDetail.listDetFromEntity());
            }
            list.add(reservation.ListResDtoFromEntity(dtoList));
        }

        return new PageImpl<>(list, pageable, reservationList.getTotalElements());
    }


    public Page<ReservationTimeResDto> reservationListByDate(ReservationListReqDto dto, Pageable pageable){
        Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> {
            log.error("reservationList() : 해당 id의 회원을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        });
        Box box = boxRepository.findByIdAndDelYn(member.getBox().getId(),"N").orElseThrow(()-> new EntityNotFoundException("해당하는 id의 박스가 존재하지 않습니다."));
        if (member.getBox()==null || !member.getBox().equals(box)) {
            log.error("reservationCreate() : 박스에 대한 권한이 없습니다.");
            throw new IllegalArgumentException("박스에 대한 권한이 없습니다.");
        }
        LocalDate date = dto.getDate();
        Page<Reservation> reservationList = reservationRepository.findByBoxAndDateAndDelYn(box, date, "N", pageable);
        return reservationList.map(Reservation::timeFromEntity);
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
        int updateNum = dto.getMaximumPeople() - reservation.getMaximumPeople();
        if (updateNum > 0) {
            reservationManagementService.increaseAvailable(reservation.getId(), updateNum);
        } else {
            reservationManagementService.decreaseAvailable(reservation.getId(), Math.abs(updateNum));
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
        reservation.updateDelYn();
        reservationRepository.save(reservation);
    }
}

