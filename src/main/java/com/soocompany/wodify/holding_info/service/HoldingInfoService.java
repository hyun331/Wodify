package com.soocompany.wodify.holding_info.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.holding_info.domain.HoldingInfo;
import com.soocompany.wodify.holding_info.dto.HoldingInfoListResDto;
import com.soocompany.wodify.holding_info.dto.HoldingInfoResDto;
import com.soocompany.wodify.holding_info.repository.HoldingInfoRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.registration_info.domain.RegistrationInfo;
import com.soocompany.wodify.registration_info.repository.RegistrationInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HoldingInfoService {
    private final HoldingInfoRepository holdingInfoRepository;
    private final MemberRepository memberRepository;
    private final RegistrationInfoRepository registrationInfoRepository;

    public HoldingInfoResDto holdingInfoCreate() {
        Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> {
            log.error("reservationList() : 해당 id의 회원을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        });
        Box box = member.getBox();
        if (box==null){
            log.error("holdingInfoCreate() : box등록 정보가 없습니다. 정지 등록 불가능");
            throw new IllegalArgumentException("box등록 정보가 없습니다.");
        }
        List<HoldingInfo> holdingInfos = holdingInfoRepository.findByMemberAndBoxAndDelYn(member, box, "N");
        if (holdingInfos.size()>=3) {
            log.error("holdingInfoCreate() : 정지는 3회로 제한됩니다.");
            throw new IllegalArgumentException("정지는 3회로 제한됩니다.");
        }
        HoldingInfo holdingInfo = HoldingInfo.builder()
                .member(member)
                .box(box)
                .holdingStart(LocalDate.now())
                .isOnHold(true)
                .build();
        HoldingInfo savedHoldingInfo = holdingInfoRepository.save(holdingInfo);
        return savedHoldingInfo.fromEntity();
    }

    public List<HoldingInfoListResDto> holdingInfoList(Long id) {
        Member member = memberRepository.findByIdAndDelYn(id, "N").orElseThrow(() -> new EntityNotFoundException("해당 id의 회원이 존재하지 않습니다."));
        Box box = member.getBox();
        if (box==null){
            throw new IllegalArgumentException("box등록 정보가 없습니다.");
        }
        List<HoldingInfo> holdingInfoList = holdingInfoRepository.findByMemberAndBoxAndDelYn(member, box, "N");
        List<HoldingInfoListResDto> listResDtos = new ArrayList<>();
        for (HoldingInfo holdingInfo : holdingInfoList) {
            listResDtos.add(holdingInfo.ListfromEntity());
        }
        return listResDtos;
    }

    public void unholding(Long id) {
        HoldingInfo holdingInfo = holdingInfoRepository.findByIdAndDelYn(id, "N").orElseThrow(() -> {
            log.error("unholding() : 해당 id의 정지정보를 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 정지정보를 찾을 수 없습니다.");
        });
        Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> {
            log.error("unholding() : 해당 id의 회원을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        });
        Box box = member.getBox();
        if (box==null){
            throw new IllegalArgumentException("box등록 정보가 없습니다.");
        }
        if (!holdingInfo.getMember().equals(member)||!holdingInfo.getBox().equals(box)) {
            log.error("unholding() : 정지정보에 대한 접근이 잘못되었습니다.");
            throw  new IllegalArgumentException("정지정보에 대한 접근이 잘못되었습니다.");
        }
        holdingInfo.updateHoldingInfo();
        LocalDate start = holdingInfo.getHoldingStart();
        LocalDate end = holdingInfo.getHoldingEnd();
        long holdingPeriod = ChronoUnit.DAYS.between(start, end);
        List<RegistrationInfo> registrationInfos = registrationInfoRepository.findByMemberAndBoxAndDelYnOrderByRegistrationDateDesc(member, box, "N");
        if (registrationInfos.isEmpty()) {
            log.error("unholding() : 박스에 등록되어있지 않은 회원입니다.");
            throw new IllegalArgumentException("박스에 등록되어있지 않은 회원입니다.");
        }
        RegistrationInfo registrationInfo = registrationInfos.get(0);
        registrationInfo.updateEndDate(holdingPeriod);

    }
}
