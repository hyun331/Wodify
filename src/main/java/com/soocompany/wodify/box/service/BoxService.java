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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    public Box boxCreate(BoxSaveReqDto dto) {
        // 대표 ID가 유일한지 확인
        Optional<Box> existingBoxByMember = boxRepository.findByMemberIdAndDelYn(dto.getRepresentativeId(), "N").stream().findFirst();
        if (existingBoxByMember.isPresent()) {
            String errorMessage = "id가 " + dto.getRepresentativeId() + "인 Member가 이미 다른 Box를 가지고 있습니다";
            log.error("boxCreate() : " + errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        // 멤버 찾기
        Optional<Member> optionalMember = memberRepository.findById(dto.getRepresentativeId());
        if (!optionalMember.isPresent()) {
            String errorMessage = "id가 " + dto.getRepresentativeId() + "인 Member를 찾을 수 없습니다";
            log.error("boxCreate() : " + errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Member member = optionalMember.get();
        Box box = new Box(dto.getName(), dto.getLogo(), dto.getOperatingHours(), dto.getFee(), dto.getIntro(), dto.getAddress(), member);
        return boxRepository.save(box);
    }


    @Transactional
    public Box boxUpdate(Long id, BoxUpdateReqDto dto) {
        Optional<Box> optionalBox = boxRepository.findByIdAndDelYn(id, "N");
        if (optionalBox.isPresent()) {
            Box box = optionalBox.get();
            box.updateDetails(
                    dto.getName(),
                    dto.getLogo(),
                    dto.getOperatingHours(),
                    dto.getFee(),
                    dto.getIntro(),
                    dto.getAdress()
            );
            return boxRepository.save(box);
        } else {
            String errorMessage = "id가 " + id + "인 Box를 찾을 수 없거나 이미 삭제되었습니다";
            log.error("boxUpdate() : " + errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }


    public void boxDelete(Long id) {
        Optional<Box> optionalBox = boxRepository.findByIdAndDelYn(id, "N");
        if (optionalBox.isPresent()) {
            Box box = optionalBox.get();
            box.updateDelYn();
            boxRepository.save(box);
        } else {
            String errorMessage = "id가 " + id + "인 Box를 찾을 수 없거나 이미 삭제되었습니다";
            log.error("boxDelete() : " + errorMessage);
            throw new IllegalArgumentException(errorMessage);
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

