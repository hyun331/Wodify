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
//        맴버id가 존재하는지 확인하고 없으면 예외발생

        Optional<Member> optionalMember = memberRepository.findById(dto.getRepresentativeId());
        if (optionalMember.isEmpty()) {
            throw new RuntimeException("Member not found with id " + dto.getRepresentativeId());
        }

        Optional<Box> existingBoxByCode = boxRepository.findByCode(dto.getCode());
        if (existingBoxByCode.isPresent() && !existingBoxByCode.get().getDelYn().equals("Y")) {
            throw new RuntimeException("Box with code " + dto.getCode() + " already exists");
        }

        Optional<Box> existingBoxByMember = boxRepository.findByMember_Id(dto.getRepresentativeId());
        if (existingBoxByMember.isPresent() && !existingBoxByMember.get().getDelYn().equals("Y")) {
            throw new RuntimeException("Member with ID " + dto.getRepresentativeId() + " is already associated with another box");
        }

        Member member = optionalMember.get();
        Box box = dto.toEntity(member);
        return boxRepository.save(box);
    }



    @Transactional
    public Box boxUpdate(Long id, BoxUpdateReqDto dto) {
//        delyn이 N일때만 수정 가능
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
            throw new RuntimeException("Box not found or already deleted with id " + id);
        }
    }



    @Transactional
    public Box boxDelete(Long id) {
        Optional<Box> optionalBox = boxRepository.findById(id);
        if (optionalBox.isPresent()) {
            Box box = optionalBox.get();
            box.markAsDeleted();
            return boxRepository.save(box);
        } else {
            throw new RuntimeException("Box not found with id" + id);
        }
    }
}
