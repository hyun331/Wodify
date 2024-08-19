package com.soocompany.wodify.registration_info.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.common.dto.EmailDto;
import com.soocompany.wodify.common.service.EmailService;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.domain.Role;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.registration_info.domain.RegistrationInfo;
import com.soocompany.wodify.registration_info.dto.RegistrationResDto;
import com.soocompany.wodify.registration_info.dto.RegistrationSaveReqDto;
import com.soocompany.wodify.registration_info.repository.RegistrationInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@Transactional
public class RegistrationService {
    private final MemberRepository memberRepository;
    private final RegistrationInfoRepository registrationInfoRepository;
    private final EmailService emailService;

    @Autowired
    public RegistrationService(MemberRepository memberRepository, RegistrationInfoRepository registrationInfoRepository, EmailService emailService) {
        this.memberRepository = memberRepository;
        this.registrationInfoRepository = registrationInfoRepository;
        this.emailService = emailService;
    }


    public RegistrationResDto registrationCreate(RegistrationSaveReqDto dto) {
        // coach, ceo만 가능
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        Member loginMember = memberRepository.findByIdAndDelYn(Long.parseLong(loginId), "N").orElseThrow(()->{
            log.error("registrationCreate() : id에 맞는 coach 또는 ceo가 없습니다");
            throw new EntityNotFoundException("id에 맞는 coach 또는 ceo가 없습니다");
        });
        Box box = loginMember.getBox();
        if(box==null){
            //현재 다니고 있는 박스가 없음
            log.error("registrationCreate() : 박스에 등록되지 않은 코치, 대표가 회원을 등록할 수 없습니다.");
            throw new IllegalArgumentException("박스에 등록되지 않은 코치, 대표가 회원을 등록할 수 없습니다.");
        }

        //등록할 회원
        Member userMember = memberRepository.findByEmailAndDelYn(dto.getEmail(), "N").orElseThrow(()->{
            log.error("registrationCreate() : 입력한 이메일에 대한 회원이 존재하지 않습니다.");
            throw new EntityNotFoundException("입력한 이메일에 대한 회원이 존재하지 않습니다.");
        });

        List<RegistrationInfo> registrationInfoList = registrationInfoRepository.findByMemberAndBoxAndDelYnOrderByRegistrationDateDesc(userMember, box, "N");
        //기존 회원인 경우 registrationDate가 기존 등록 종료일보다 전이면 안됨
        if(!registrationInfoList.isEmpty()){
            //가장 최신 등록
            RegistrationInfo latelyRegistrationInfo = registrationInfoList.get(0);
            if(dto.getRegistrationDate().isBefore(latelyRegistrationInfo.getEndDate())){
                log.error("registrationCreate() : 기존 회원 등록(연장) 시 등록일이 이전 등록의 종료일보다 이전이면 안됩니다.");
                throw new IllegalArgumentException("기존 회원 등록(연장) 시 등록일이 이전 등록의 종료일보다 이전이면 안됩니다.");
            }

        }
        LocalDate endDate = dto.getRegistrationDate().plusMonths(dto.getRegistrationMonth());
        RegistrationInfo newRegistration = registrationInfoRepository.save(dto.toEntity(userMember, box, endDate));


        //member table에도 적용되야함
        userMember.memberBoxUpdate(box);
        EmailDto emailDto = EmailDto.builder()
                .receiverEmail(dto.getEmail())
                .emailTitle(box.getName()+" 박스 등록/연장 이메일 공지")
                .emailContent("회원님의 박스 등록기간이 변경되었습니다. <br> "+dto.getRegistrationDate()+" ~ "+dto.getRegistrationDate().plusMonths(dto.getRegistrationMonth()))
                .build();
        emailService.sendEmail(emailDto);

        return newRegistration.fromEntity() ;
    }

    //박스 등록 정보
    public Page<RegistrationResDto> boxRegistrationList(Pageable pageable) {
        Member loginMember = memberRepository.findByIdAndDelYn(Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName()), "N").orElseThrow(()->{
            log.error("boxRegistrationList() : id에 맞는 회원이 존재하지 않습니다.");
            throw new EntityNotFoundException("id에 맞는 회원이 존재하지 않습니다.");
        });

        Page<RegistrationInfo> registrationInfos = registrationInfoRepository.findAllByBoxAndDelYnOrderByCreatedTimeDesc(pageable, loginMember.getBox(), "N");
        return registrationInfos.map(a->a.fromEntity());
    }
}
