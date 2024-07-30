package com.soocompany.wodify.wod.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.repository.BoxRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.wod.domain.Wod;
import com.soocompany.wodify.wod.domain.WodDetail;
import com.soocompany.wodify.wod.dto.WodDetSaveReqDto;
import com.soocompany.wodify.wod.dto.WodSaveReqDto;
import com.soocompany.wodify.wod.repository.WodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class WodService {
    private final WodRepository wodRepository;
    private final MemberRepository memberRepository;
    private final BoxRepository boxRepository;

    public Wod wodSave(WodSaveReqDto wodSaveReqDto, String memberEmail) {
//        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmailAndDelYn(memberEmail, "N").orElseThrow(() -> new EntityNotFoundException("member is not found"));
        Box box = boxRepository.findByMember_Id(member.getId()).orElseThrow(() -> new EntityNotFoundException("box is not found"));
        Wod wod = Wod.builder()
                .box(box)
                .member(member)
                .date(wodSaveReqDto.getDate())
                .timeCap(wodSaveReqDto.getTimeCap())
                .rounds(wodSaveReqDto.getRounds())
                .info(wodSaveReqDto.getInfo())
                .build();
        for (WodDetSaveReqDto wodDetSaveReqDto : wodSaveReqDto.getWodDetSaveReqDtoList()) {
            WodDetail wodDetail = WodDetail.builder()
                    .name(wodDetSaveReqDto.getName())
                    .contents(wodDetSaveReqDto.getContents())
                    .build();
            wod.getWodDetails().add(wodDetail);
        }
        return wodRepository.save(wod);
    }
//
//    public WodResDto wod(Date date) {
//        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        Member member = memberRepository.findByEmailAndDelYn(memberEmail, "N").orElseThrow(() -> new EntityNotFoundException("member is not found"));
//        Box box = boxRepository.findByMember_Id(member.getId()).orElseThrow(() -> new EntityNotFoundException("box is not found"));
//        Long boxId = box.getId();
//        Wod wod = wodRepository.findByBoxIdAndDateAndDelYn(date).orElseThrow(() -> new EntityNotFoundException("Wod is not found"));
//        return new WodResDto().fromEntity(wod);
//    }
//
//    public Wod wodDelete(Long id) {
//        Wod wod = wodRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("order is not found")); ordering.updateOrderStatus(OrderStatus.CANCELED); return ordering; }
}
