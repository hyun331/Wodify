package com.soocompany.wodify.reservation_detail.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.holding_info.domain.HoldingInfo;
import com.soocompany.wodify.holding_info.repository.HoldingInfoRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.registration_info.domain.RegistrationInfo;
import com.soocompany.wodify.registration_info.repository.RegistrationInfoRepository;
import com.soocompany.wodify.reservation.service.ReservationManagementService;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetCreateReqDto;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetailDetResDto;
import com.soocompany.wodify.reservation_detail.repository.ReservationDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReservationDetailService {
    private final ReservationDetailRepository reservationDetailRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final RegistrationInfoRepository registrationInfoRepository;
    private final HoldingInfoRepository holdingInfoRepository;
    private final ReservationManagementService reservationManagementService;

    public ReservationDetailDetResDto reservationCreate(ReservationDetCreateReqDto dto) {
        Reservation reservation = reservationRepository.findByIdAndDelYn(dto.getReservationId(), "N").orElseThrow(() -> new EntityNotFoundException("해당 id의 예약이 존재하지 않습니다."));
        Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> {
            log.error("reservationCreate() : 해당 id의 회원을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        });
        // 예약을 요청한 회원의 자격 요건 확인 = 등록 정보가 있는지 & 정지 여부
        Box box = member.getBox();
        if (box==null) {
            log.error("reservationCreate() : 박스에 소속되어있지 않은 회원은 예약이 불가합니다.");
            throw new IllegalArgumentException("박스에 소속되어있지 않은 회원은 예약이 불가합니다.");
        }

//        정지 상태 확인
        List<HoldingInfo> holdingInfos = holdingInfoRepository.findByMemberAndBoxAndDelYn(member, box, "N");
        for (HoldingInfo holdingInfo : holdingInfos) {
            if (holdingInfo.isOnHold()) {
                log.error("reservationCreate() : 정지 기간 내 예약은 불가합니다.");
                throw new IllegalArgumentException("정지 기간 내 예약은 불가합니다.");
            }
        }

        if (reservationManagementService.decreaseAvailable(reservation.getId()) < 0) {
            throw new IllegalStateException("예약 인원이 초과되어 예약이 불가능합니다.");
        }
        ReservationDetail reservationDetail = dto.toEntity(reservation, member);
        ReservationDetail savedDetail = reservationDetailRepository.save(reservationDetail);
        return savedDetail.detFromEntity();
    }

    public ReservationDetailDetResDto detail(Long id) {
        ReservationDetail reservationDetail = reservationDetailRepository.findByIdAndDelYn(id, "N").orElseThrow(() -> new EntityNotFoundException("해당 id의 예약상세내역을 찾을 수 없습니다."));
        return reservationDetail.detFromEntity();
    }

    public Page<ReservationDetailDetResDto> listByMember(Long memberId, @PageableDefault(sort = "date",direction = Sort.Direction.DESC) Pageable pageable) {
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다."));
        Page<ReservationDetail> details = reservationDetailRepository.findByMemberAndDelYn(member, "N", pageable);
        return details.map(ReservationDetail::detFromEntity);
    }

    public void delete(Long id) {
        ReservationDetail reservationDetail = reservationDetailRepository.findByIdAndDelYn(id, "N").orElseThrow(() -> new EntityNotFoundException("해당 id의 예약이 없습니다."));
        Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> {
            log.error("delete() : 해당 id의 회원을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        });
        if (!reservationDetail.getMember().equals(member)) {
            log.error("delete() : 예약을 삭제할 수 있는 권한이 없습니다.");
            throw new IllegalArgumentException("예약을 삭제할 수 있는 권한이 없습니다.");
        }
        reservationDetail.updateDelYn();
        reservationDetailRepository.save(reservationDetail);
    }
}