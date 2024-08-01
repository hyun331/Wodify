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

import javax.transaction.Transactional;
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
                    return new IllegalArgumentException(errorMessage);
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


    public void boxDelete(Long id) {
        // 현재 로그인한 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();

        // Box 조회 및 소유자 확인
        Box box = boxRepository.findByIdAndDelYn(id, "N")
                .orElseThrow(() -> {
                    String errorMessage = "id가 " + id + "인 Box를 찾을 수 없거나 이미 삭제되었습니다";
                    log.error("boxDelete() : " + errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        if (!box.getMember().getId().toString().equals(memberId)) {
            String errorMessage = "현재 사용자가 해당 Box를 삭제할 권한이 없습니다.";
            log.error("boxDelete() : " + errorMessage);
            throw new SecurityException(errorMessage);
        }

        // Box 삭제
        box.updateDelYn();
        boxRepository.save(box);
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
                    return new IllegalArgumentException(errorMessage);
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

