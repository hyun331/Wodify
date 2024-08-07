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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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


    public Box  boxCreate(BoxSaveReqDto dto) throws IOException {
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

        //맴버에 박스 넣어주기
        Member newMember = memberRepository.findByEmailAndDelYn(member.getEmail(), "N").orElseThrow(()->new EntityNotFoundException("Test Ceo Initial Data Loader Exception"));


        // MultipartFile에서 파일 저장 경로 얻기
        MultipartFile logoFile = dto.getLogoPath();
        String logoPath = null;
        if (logoFile != null && !logoFile.isEmpty()) {
            byte[] bytes = logoFile.getBytes();
            Path path = Paths.get("C:/Users/rnjsc/Desktop/tmp/", UUID.randomUUID() + "_" + logoFile.getOriginalFilename());
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            logoPath = path.toString();
        }

        Box box = boxRepository.save(dto.toEntity(member));
        if (logoPath != null) {
            box.updateLogo(logoPath);
            boxRepository.save(box);
        }

        newMember.memberBoxUpdate(box);
        memberRepository.save(newMember);
        return box;
    }


    public Box boxUpdate(Long id, BoxUpdateReqDto dto) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();

        Box box = boxRepository.findByIdAndDelYn(id, "N")
                .orElseThrow(() -> {
                    String errorMessage = "id가 " + id + "인 Box를 찾을 수 없거나 이미 삭제되었습니다";
                    log.error("boxUpdate() : " + errorMessage);
                    throw new IllegalArgumentException(errorMessage);
                });

        if (!box.getMember().getId().toString().equals(memberId)) {
            String errorMessage = "현재 사용자가 해당 Box를 수정할 권한이 없습니다.";
            log.error("boxUpdate() : " + errorMessage);
            throw new SecurityException(errorMessage);
        }

        // 파일 저장 로직 처리
        MultipartFile logoFile = dto.getLogo();
        if (logoFile != null && !logoFile.isEmpty()) {
            byte[] bytes = logoFile.getBytes();
            Path path = Paths.get("C:/Users/rnjsc/Desktop/tmp/", UUID.randomUUID() + "_" + logoFile.getOriginalFilename());
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            box.updateLogo(path.toString());
        }

        // 로고 업데이트를 제외한 다른 필드만 업데이트
        box.updateDetails(dto.getName(),
                box.getLogo(),
                dto.getOperatingHours(),
                dto.getFee(),
                dto.getIntro(),
                dto.getAddress()
        );

        return boxRepository.save(box);
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
                    throw new IllegalArgumentException(errorMessage);
                });

        if (!box.getMember().getId().toString().equals(memberId)) {
            String errorMessage = "현재 사용자가 해당 Box를 삭제할 권한이 없습니다.";
            log.error("boxDelete() : " + errorMessage);
            throw new IllegalArgumentException(errorMessage);
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

