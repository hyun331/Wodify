package com.soocompany.wodify.wod.service;
import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.repository.BoxRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.domain.Role;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import com.soocompany.wodify.wod.domain.Wod;
import com.soocompany.wodify.wod.domain.WodDetail;
import com.soocompany.wodify.wod.dto.WodDetResDto;
import com.soocompany.wodify.wod.dto.WodDetSaveReqDto;
import com.soocompany.wodify.wod.dto.WodResDto;
import com.soocompany.wodify.wod.dto.WodSaveReqDto;
import com.soocompany.wodify.wod.repository.WodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class WodService {
    private final WodRepository wodRepository;
    private final MemberRepository memberRepository;
    private final BoxRepository boxRepository;
    private final ReservationRepository reservationRepository;

    public Wod wodSave(WodSaveReqDto wodSaveReqDto) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String email = wodSaveReqDto.getEmail();
        log.error("wodSave() : 해당 Email 의 멤버를 찾을 수 없습니다.");
        Member member = memberRepository.findByEmailAndDelYn(email, "N")
            .orElseThrow(() -> new EntityNotFoundException("해당 Email 의 멤버를 찾을 수 없습니다."));
        log.error("wodSave() : WOD 생성 권한이 없습니다.");
        if (member.getRole() == Role.USER) {throw new IllegalArgumentException("WOD 생성 권한이 없습니다.");}
        Box box = member.getBox();
        log.error("wodSave() : 소속된 박스가 없습니다.");
        if (box == null) {throw new IllegalArgumentException("소속된 박스가 없습니다.");}
        Optional<Wod> savedWod = wodRepository.findByBoxIdAndDateAndDelYn(box.getId(), wodSaveReqDto.getDate(), "N");
        log.error("wodSave() : 이미 WOD 가 존재합니다.");
        if (savedWod.isPresent()) {throw new IllegalArgumentException("이미 WOD 가 존재합니다.");}
        Wod wod = wodSaveReqDto.toEntity(member, box);
        for (WodDetSaveReqDto wodDetSaveReqDto : wodSaveReqDto.getWodDetSaveReqDtoList())
            wod.getWodDetails().add(wodDetSaveReqDto.toEntity(wod));
        return wodRepository.save(wod);
    }

    public WodResDto wodFind(String email, LocalDate date) {
//        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.error("wodFind() : 해당 이메일의 멤버를 찾을 수 없습니다.");
        Member member = memberRepository.findByEmailAndDelYn(email, "N")
            .orElseThrow(() -> new EntityNotFoundException("해당 이메일의 멤버를 찾을 수 없습니다."));
        log.error("wodFind() : 해당 ID의 박스를 찾을 수 없습니다.");
        Box box = boxRepository.findByMember_Id(member.getBox().getId())
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 박스를 찾을 수 없습니다."));
        log.error("wodFind() : 해당 날짜에 등록된 WOD 가 없습니다.");
        Wod wod = wodRepository.findByBoxIdAndDateAndDelYn(box.getId(), date, "N")
            .orElseThrow(() -> new EntityNotFoundException("해당 날짜에 등록된 WOD 가 없습니다."));
        WodResDto wodResDto = WodResDto.fromEntity(wod);
        for (WodDetail wodDetail : wod.getWodDetails()) {
            wodResDto.getWodDetResDtoList().add(WodDetResDto.fromEntity(wodDetail));
        }
        return wodResDto;
    }

    public Wod wodDelete(String email, LocalDate date) {
        Member member = memberRepository.findByEmailAndDelYn(email, "N")
            .orElseThrow(() -> new EntityNotFoundException("해당 이메일의 멤버를 찾을 수 없습니다."));
        Box box = boxRepository.findByMember_Id(member.getBox().getId())
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 박스를 찾을 수 없습니다."));
        Wod wod = wodRepository.findByBoxIdAndDateAndDelYn(box.getId(), date, "N")
            .orElseThrow(() -> new EntityNotFoundException("해당 날짜에 등록된 WOD가 없습니다."));
        //  예약 여부 확인 후 예약이 생성되어 있으면 예외 발생. reservationRepository.findByWodId 함수 필요
        return wod.wodDelete();
    }
}
