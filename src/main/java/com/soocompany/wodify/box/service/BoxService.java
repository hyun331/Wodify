package com.soocompany.wodify.box.service;

// BoxService.java
import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.dto.BoxSaveReqDto;
import com.soocompany.wodify.box.dto.BoxUpdateReqDto;
import com.soocompany.wodify.box.repository.BoxRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BoxService {

    private final BoxRepository boxRepository;
    private final MemberRepository memberRepository; // 추가된 필드

    @Autowired
    public BoxService(BoxRepository boxRepository, MemberRepository memberRepository) {
        this.boxRepository = boxRepository;
        this.memberRepository = memberRepository;
    }



    @Transactional
    public Box boxCreate(BoxSaveReqDto dto) {
        // 코드가 유일한지 확인
//        List<Box> existingBoxesByCode = boxRepository.findByCodeAndDelYn(dto.getCode(), "N");
//        if (!existingBoxesByCode.isEmpty()) {
//            throw new RuntimeException("boxCreate() : 코드가 " + dto.getCode() + "인 Box가 이미 존재합니다");
//        }

        // 대표 ID가 유일한지 확인
        List<Box> existingBoxesByMember = boxRepository.findByMember_IdAndDelYn(dto.getRepresentativeId(), "N");
        if (!existingBoxesByMember.isEmpty()) {
            throw new RuntimeException("boxCreate() : id가 " + dto.getRepresentativeId() + "인 대표가 이미 다른 Box를 운영중입니다");
        }

        // 멤버 찾기
        Optional<Member> optionalMember = memberRepository.findById(dto.getRepresentativeId());
        if (optionalMember.isEmpty()) {
            throw new RuntimeException("boxCreate() : id가 " + dto.getRepresentativeId() + "인 대표를 찾을 수 없습니다");
        }

        Member member = optionalMember.get();
        Box box = dto.toEntity(member);
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
                    dto.getIntro()
            );
            return boxRepository.save(box);
        } else {
            throw new RuntimeException("boxUpdate() : id가 " + id + "인 Box를 찾을 수 없거나 이미 삭제되었습니다");
        }
    }




    @Transactional
    public Box boxDelete(Long id) {
        Optional<Box> optionalBox = boxRepository.findByIdAndDelYn(id, "N");
        if (optionalBox.isPresent()) {
            Box box = optionalBox.get();
            box.updateDelYn();
            return boxRepository.save(box);
        } else {
            throw new RuntimeException("boxDelete() : id가 " + id + "인 박스를 찾을 수 없거나 이미 삭제되었습니다");
        }
    }
}