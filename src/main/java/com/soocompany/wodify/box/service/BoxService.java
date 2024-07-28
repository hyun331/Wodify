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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
            throw new IllegalArgumentException("id가 " + dto.getRepresentativeId() + "인 Member가 이미 다른 Box와 연관되어 있습니다");
        }

        // 멤버 찾기
        Optional<Member> optionalMember = memberRepository.findById(dto.getRepresentativeId());
        if (optionalMember.isEmpty()) {
            throw new IllegalArgumentException("id가 " + dto.getRepresentativeId() + "인 Member를 찾을 수 없습니다");
        }

        Member member = optionalMember.get();
        Box box = dto.toEntity(member);
        return boxRepository.save(box);
    }



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
            throw new IllegalArgumentException("id가 " + id + "인 Box를 찾을 수 없거나 이미 삭제되었습니다");
        }
    }



    public void boxDelete(Long id) {
        Optional<Box> optionalBox = boxRepository.findByIdAndDelYn(id, "N");
        if (optionalBox.isPresent()) {
            Box box = optionalBox.get();
            box.updateDelYn();
            boxRepository.save(box);
        } else {
            throw new IllegalArgumentException("id가 " + id + "인 Box를 찾을 수 없거나 이미 삭제되었습니다");
        }
    }



    public Page<Box> boxList(Pageable pageable) {
        return boxRepository.findAllByDelYn("N", pageable);
    }



    public Box boxDetail(Long id) {
        return boxRepository.findByIdAndDelYn(id, "N")
                .orElseThrow(() -> new IllegalArgumentException("id가 " + id + "인 Box를 찾을 수 없거나 이미 삭제되었습니다"));
    }
}