package com.soocompany.wodify.box.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.dto.*;
import com.soocompany.wodify.box.repository.BoxRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class BoxService {

    private final BoxRepository boxRepository;
    private final MemberRepository memberRepository;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    @Autowired
    public BoxService(BoxRepository boxRepository, MemberRepository memberRepository, S3Client s3Client) {
        this.boxRepository = boxRepository;
        this.memberRepository = memberRepository;
        this.s3Client = s3Client;
    }


    public Long boxCreate(BoxSaveReqDto dto) {
        Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        Member loginMember = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(()->{
            log.error("boxCreate() : id에 맞는 회원(ceo)가 존재하지 않습니다.");
            throw new EntityNotFoundException("id에 맞는 회원(ceo)가 존재하지 않습니다.");
        });

        if(loginMember.getBox() != null){
            String errorMessage = "id가 " + memberId + "인 Member가 이미 다른 Box를 가지고 있습니다";
            log.error("boxCreate() : " + errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        MultipartFile logoFile = dto.getLogoPath();
        String s3Path = null;
        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                byte[] bytes = logoFile.getBytes();
                String fileName = UUID.randomUUID() + "_" + logoFile.getOriginalFilename();

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileName)
                        .build();
                PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest,RequestBody.fromBytes(bytes));
                s3Path = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(fileName)).toExternalForm();
            } catch (IOException e) {
                log.error("boxCreate() : 이미지 저장 실패");
                throw new RuntimeException("이미지 저장 실패", e);
            }
        }

        Box box = boxRepository.save(dto.toEntity(loginMember));
        if (s3Path != null) {
            box.updateLogo(s3Path);
            boxRepository.save(box);
        }
        loginMember.memberBoxUpdate(box);

        return box.getId();
    }




    public BoxSaveReqDto boxUpdate(BoxUpdateReqDto dto){
        // 현재 로그인한 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();

        // 로그인한 사용자의 Box를 찾음
        Box box = boxRepository.findByMemberIdAndDelYn(Long.parseLong(memberId), "N")
                .stream()
                .findFirst()
                .orElseThrow(() -> {
                    String errorMessage = "해당 사용자의 Box를 찾을 수 없습니다.";
                    log.error("boxUpdate() : " + errorMessage);
                    throw new EntityNotFoundException(errorMessage);
                });

        // 파일 저장 로직 처리 및 AWS S3에 업로드
        MultipartFile logoFile = dto.getLogo();
        if (logoFile != null && !logoFile.isEmpty()) {
            byte[] bytes;
            try {
                bytes = logoFile.getBytes();
            } catch (IOException e) {
                throw new RuntimeException("이미지 처리 중 오류가 발생했습니다.", e);
            }
            String fileName = UUID.randomUUID() + "_" + logoFile.getOriginalFilename();
            Path path = Paths.get("C:/Users/rnjsc/Desktop/tmp/", fileName);

            // Local PC에 임시 저장
            try {
                Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패", e);
            }

            // AWS S3에 업로드
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(path));
            String s3Path = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(fileName)).toExternalForm();
            box.updateLogo(s3Path);
        }

        // null 값 처리: null인 필드는 기존 값을 유지
        box.updateDetails(
                dto.getName() != null ? dto.getName() : box.getName(),
                box.getLogoPath(),  // 로고는 따로 처리하므로 여기서는 변경하지 않음
                dto.getOperatingHours() != null ? dto.getOperatingHours() : box.getOperatingHours(),
                dto.getFee() != null ? dto.getFee() : box.getFee(),
                dto.getIntro() != null ? dto.getIntro() : box.getIntro(),
                dto.getAddress() != null ? dto.getAddress() : box.getAddress()
        );

        Box updatedBox = boxRepository.save(box);
        return BoxSaveReqDto.fromEntity(updatedBox);
    }


    //박스 폐업 -> 대표, 코치, 회원의 box 정보 null변경
    public void boxDelete() {
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()), "N").orElseThrow(() -> {
            log.error("boxDelete() : id에 맞는 member가 존재하지 않습니다.");
            throw new EntityNotFoundException("id에 맞는 member가 존재하지 않습니다.");
        });

        Box box = member.getBox();
        if (box == null) {
            log.error("boxDelete() : 대표의 box가 존재하지 않습니다.");
            throw new EntityNotFoundException("대표의 box가 존재하지 않습니다.");
        }

        // Box 삭제
        box.updateDelYn();
        //박스에 연관된 코치, 회원 리스트
        List<Member> boxMemberList = memberRepository.findByBoxAndDelYn(box, "N");
        for (Member m : boxMemberList) {
            m.memberBoxUpdate(null);
        }

        member.memberBoxUpdate(null);

    }


    public Page<BoxListResDto> boxList(BoxSearchDto searchDto, Pageable pageable) {
        Specification<Box> specification = (Root<Box> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (searchDto.getSearchName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchDto.getSearchName() + "%"));
            }

            if (searchDto.getSearchAddress() != null) {
                predicates.add(criteriaBuilder.like(root.get("address"), "%" + searchDto.getSearchAddress() + "%"));
            }

            predicates.add(criteriaBuilder.equal(root.get("delYn"), "N"));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Box> boxPage = boxRepository.findAll(specification, pageable);
        return boxPage.map(box -> BoxListResDto.builder()
                .id(box.getId())
                .logoPath(box.getLogoPath())
                .name(box.getName())
                .address(box.getAddress())
                .operatingHours(box.getOperatingHours())
                .fee(box.getFee())
                .build());
    }


    public BoxDetailResDto boxDetail(Long id) {
        Box box = boxRepository.findByIdAndDelYn(id, "N")
                .orElseThrow(() -> {
                    String errorMessage = "id가 " + id + "인 Box를 찾을 수 없거나 이미 삭제되었습니다";
                    log.error("boxDetail() : " + errorMessage);
                    throw new IllegalArgumentException(errorMessage);
                });

        return BoxDetailResDto.builder()
                .id(box.getId())
                .name(box.getName())
                .logo(box.getLogoPath())
                .operatingHours(box.getOperatingHours())
                .fee(box.getFee())
                .address(box.getAddress())
                .intro(box.getIntro())
                .build();
    }

    public BoxDetailResDto myBox() {
        // 현재 로그인한 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();

        // 로그인한 사용자의 Member 객체를 가져오기
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(memberId), "N")
                .orElseThrow(() -> {
                    String errorMessage = "해당 사용자를 찾을 수 없습니다.";
                    log.error("getBoxDetailForCurrentUser() : " + errorMessage);
                    throw new EntityNotFoundException(errorMessage);
                });

        // 사용자의 box_id와 일치하는 Box를 찾기
        Box box = boxRepository.findByIdAndDelYn(member.getBox().getId(), "N")
                .orElseThrow(() -> {
                    String errorMessage = "해당 Box를 찾을 수 없습니다.";
                    log.error("getBoxDetailForCurrentUser() : " + errorMessage);
                    throw new EntityNotFoundException(errorMessage);
                });

        return BoxDetailResDto.builder()
                .id(box.getId())
                .name(box.getName())
                .logo(box.getLogoPath())
                .operatingHours(box.getOperatingHours())
                .fee(box.getFee())
                .address(box.getAddress())
                .intro(box.getIntro())
                .build();
    }

    public String boxName() {
        Long memberId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByIdAndDelYn(memberId, "N").orElseThrow(() -> {
            log.error("delete() : 해당 id의 회원을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 회원을 찾을 수 없습니다.");
        });
        String name = member.getBox().getName();
        return name;
    }
}

