package com.soocompany.wodify.common.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.repository.BoxRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.domain.Role;
import com.soocompany.wodify.member.dto.MemberSaveReqDto;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

@Component
public class InitialCeoDataLoader implements CommandLineRunner {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoxRepository boxRepository;

    @Override
    public void run(String... args) throws Exception {
        if(memberService.isMemberExist("hongildong@naver.com", "N")==null){
            MemberSaveReqDto member = MemberSaveReqDto.builder()
                    .name("홍길동")
                    .email("hongildong@naver.com")
                    .phone("010-1234-1234")
                    .role(Role.CEO)
                    .address("보라매로 87")
                    .deadLift(BigDecimal.valueOf(80.5))
                    .squat(BigDecimal.valueOf(60.5))
                    .benchPress(BigDecimal.valueOf(40.5))
                    .build();
            memberService.memberRegister(member);


            //박스 넣어주기
            Member newMember = memberRepository.findByEmailAndDelYn("hongildong@naver.com", "N").orElseThrow(()->new EntityNotFoundException("Test Ceo Initial Data Loader Exception"));

            Box box = Box.builder().name("홍길동의 크로스핏")
                    .logoPath("https://jiho-file.s3.ap-southeast-2.amazonaws.com/9d9d58b9-4687-4422-b302-629d2fc87b7e_%ED%81%AC%EB%A1%9C%EC%8A%A4%ED%95%8F4.png")
                    .fee("1개월 10만원")
                    .intro("대충 홍길동 크로스핏에 대한 설명")
                    .operatingHours("09:00-23:00")
                    .code("hongildong1234")
                    .member(newMember)
                    .build();

            boxRepository.save(box);

            //홍길동에게 박스 연결
            newMember.memberBoxUpdate(box);
            memberRepository.save(newMember);

        }

        if(memberService.isMemberExist("sus03319@naver.com", "N")==null){
            MemberSaveReqDto member = MemberSaveReqDto.builder()
                    .name("신승현")
                    .email("sus03319@naver.com")
                    .phone("010-1231-6484")
                    .role(Role.USER)
                    .address("보라매로 87")
                    .deadLift(BigDecimal.valueOf(80.5))
                    .squat(BigDecimal.valueOf(60.5))
                    .benchPress(BigDecimal.valueOf(40.5))
                    .build();
            memberService.memberRegister(member);
        }
        if(memberService.isMemberExist("2rhdwndmswl@naver.com", "N")==null){
            MemberSaveReqDto member = MemberSaveReqDto.builder()
                    .name("방은지")
                    .email("2rhdwndmswl@naver.com")
                    .phone("010-2534-1434")
                    .role(Role.USER)
                    .address("보라매로 87")
                    .deadLift(BigDecimal.valueOf(80.5))
                    .squat(BigDecimal.valueOf(60.5))
                    .benchPress(BigDecimal.valueOf(40.5))
                    .build();
            memberService.memberRegister(member);
        }
        if(memberService.isMemberExist("eraltndus@gmail.com", "N")==null){
            MemberSaveReqDto member = MemberSaveReqDto.builder()
                    .name("김수연")
                    .email("eraltndus@gmail.com")
                    .phone("010-1324-8908")
                    .role(Role.CEO)
                    .address("보라매로 87")
                    .deadLift(BigDecimal.valueOf(80.5))
                    .squat(BigDecimal.valueOf(60.5))
                    .benchPress(BigDecimal.valueOf(40.5))
                    .build();
            memberService.memberRegister(member);
        }
        if(memberService.isMemberExist("tjseh531@daum.net", "N")==null){
            MemberSaveReqDto member = MemberSaveReqDto.builder()
                    .name("김지호")
                    .email("tjseh531@daum.net")
                    .phone("010-1423-7854")
                    .role(Role.CEO)
                    .address("보라매로 87")
                    .deadLift(BigDecimal.valueOf(80.5))
                    .squat(BigDecimal.valueOf(60.5))
                    .benchPress(BigDecimal.valueOf(40.5))
                    .build();
            memberService.memberRegister(member);
        }
        if(memberService.isMemberExist("tls99rnjs@nate.com", "N")==null){
            MemberSaveReqDto member = MemberSaveReqDto.builder()
                    .name("권채훈")
                    .email("tls99rnjs@nate.com")
                    .phone("010-1231-6484")
                    .role(Role.CEO)
                    .address("보라매로 87")
                    .deadLift(BigDecimal.valueOf(80.5))
                    .squat(BigDecimal.valueOf(60.5))
                    .benchPress(BigDecimal.valueOf(40.5))
                    .build();
            memberService.memberRegister(member);
        }
        if(memberService.isMemberExist("eraltndus@daum.net", "N")==null){
            MemberSaveReqDto member = MemberSaveReqDto.builder()
                    .name("김수연")
                    .email("eraltndus@daum.net")
                    .phone("010-1231-6484")
                    .role(Role.CEO)
                    .address("보라매로 87")
                    .deadLift(BigDecimal.valueOf(80.5))
                    .squat(BigDecimal.valueOf(60.5))
                    .benchPress(BigDecimal.valueOf(40.5))
                    .build();
            memberService.memberRegister(member);
        }




    }
}
