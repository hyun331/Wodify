package com.soocompany.wodify.holding_info.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.repository.BoxRepository;
import com.soocompany.wodify.holding_info.domain.HoldingInfo;
import com.soocompany.wodify.holding_info.dto.HoldingInfoCreateReqDto;
import com.soocompany.wodify.holding_info.dto.HoldingInfoListResDto;
import com.soocompany.wodify.holding_info.dto.HoldingInfoResDto;
import com.soocompany.wodify.holding_info.repository.HoldingInfoRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HoldingInfoService {
    private final HoldingInfoRepository holdingInfoRepository;
    private final MemberRepository memberRepository;

    public HoldingInfoResDto holdingInfoCreate(HoldingInfoCreateReqDto dto) {
        Member member = memberRepository.findByIdAndDelYn(dto.getMemberId(), "N").orElseThrow(() -> new EntityNotFoundException("해당 id의 회원이 존재하지 않습니다."));
        Box box = member.getBox();
        if (box==null){
            log.error("holdingInfoCreate() : box등록 정보가 없습니다. 정지 등록 불가능");
            throw new IllegalArgumentException("box등록 정보가 없습니다.");
        }
        HoldingInfo holdingInfo = dto.toEntity(member, box);
        HoldingInfo savedHoldingInfo = holdingInfoRepository.save(holdingInfo);
        LocalDate holdingStart = dto.getHoldingStart();
        LocalDate holdingEnd = dto.getHoldingEnd();
        if (holdingStart.isAfter(holdingEnd)) {
            throw new IllegalArgumentException("정지시작일은 정지종료일보다 빨라야합니다.");
        }

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
}
