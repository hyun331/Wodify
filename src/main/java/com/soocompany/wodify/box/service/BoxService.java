package com.soocompany.wodify.box.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.dto.BoxDetailResDto;
import com.soocompany.wodify.box.dto.BoxListResDto;
import com.soocompany.wodify.box.dto.BoxSaveReqDto;
import com.soocompany.wodify.box.dto.BoxUpdateReqDto;
import com.soocompany.wodify.box.repository.BoxRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class BoxService {

    private final BoxRepository boxRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public BoxService(BoxRepository boxRepository, MemberRepository memberRepository) {
        this.boxRepository = boxRepository;
        this.memberRepository = memberRepository;
    }


    public BoxSaveReqDto boxCreate(BoxSaveReqDto dto) {
        // 현재 로그인한 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();

        // 대표 ID가 유일한지 확인
        Optional<Box> existingBoxByMember = boxRepository.findByMemberIdAndDelYn(Long.parseLong(memberId), "N").stream().findFirst();
        if (existingBoxByMember.isPresent()) {
            String errorMessage = "id가 " + memberId + "인 Member가 이미 다른 Box를 가지고 있습니다";
            log.error("boxCreate() : " + errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Member member = memberRepository.getReferenceById(Long.parseLong(memberId));
        Box box = new Box(dto.getName(), dto.getLogo(), dto.getOperatingHours(), dto.getFee(), dto.getIntro(), dto.getAddress(), member);
        boxRepository.save(box);
        member.memberBoxUpdate(box);
        return dto;
    }


    public BoxSaveReqDto boxUpdate(Long id, BoxUpdateReqDto dto) {
        // 현재 로그인한 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();

        // Box 조회 및 소유자 확인
        Box box = boxRepository.findByIdAndDelYn(id, "N")
                .orElseThrow(() -> {
                    String errorMessage = "id가 " + id + "인 Box를 찾을 수 없거나 이미 삭제되었습니다";
                    log.error("boxUpdate() : " + errorMessage);
                    throw  new IllegalArgumentException(errorMessage);
                });

        if (!box.getMember().getId().toString().equals(memberId)) {
            String errorMessage = "현재 사용자가 해당 Box를 수정할 권한이 없습니다.";
            log.error("boxUpdate() : " + errorMessage);
            throw new SecurityException(errorMessage);
        }

        // Box 업데이트
        box.updateDetails(dto.getName(),
                dto.getLogo(),
                dto.getOperatingHours(),
                dto.getFee(),
                dto.getIntro(),
                dto.getAddress()
        );
        boxRepository.save(box);
        return BoxSaveReqDto.builder()
                .name(box.getName())
                .logo(box.getLogo())
                .operatingHours(box.getOperatingHours())
                .fee(box.getFee())
                .intro(box.getIntro())
                .address(box.getAddress())
                .build();
    }

    //박스 폐업 -> 대표, 코치, 회원의 box 정보 null변경
    public void boxDelete() {
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()), "N").orElseThrow(()->{
            log.error("boxDelete() : id에 맞는 member가 존재하지 않습니다.");
            throw new EntityNotFoundException("id에 맞는 member가 존재하지 않습니다.");
        });

        Box box = member.getBox();
        if(box==null){
            log.error("boxDelete() : 대표의 box가 존재하지 않습니다.");
            throw new EntityNotFoundException("대표의 box가 존재하지 않습니다.");
        }
        // Box 삭제
        box.updateDelYn();


        //박스에 연관된 코치, 회원 리스트
        List<Member> boxMemberList = memberRepository.findByBoxAndDelYn(box, "N");
        for(Member m: boxMemberList){
            m.memberBoxUpdate(null);
        }

    }


    public Page<BoxListResDto> boxList(Pageable pageable) {
        Page<Box> boxPage = boxRepository.findAllByDelYn("N", pageable);
        return boxPage.map(box -> BoxListResDto.builder()
                .name(box.getName())
                .logo(box.getLogo())
                .operatingHours(box.getOperatingHours())
                .address(box.getAddress())
                .build());
    }


    public BoxDetailResDto boxDetail(Long id) {
        Box box = boxRepository.findByIdAndDelYn(id, "N")
                .orElseThrow(() -> {
                    String errorMessage = "id가 " + id + "인 Box를 찾을 수 없거나 이미 삭제되었습니다";
                    log.error("boxDetail() : " + errorMessage);
                    throw  new IllegalArgumentException(errorMessage);
                });

        return BoxDetailResDto.builder()
                .name(box.getName())
                .logo(box.getLogo())
                .operatingHours(box.getOperatingHours())
                .fee(box.getFee())
                .address(box.getAddress())
                .build();
    }
}

