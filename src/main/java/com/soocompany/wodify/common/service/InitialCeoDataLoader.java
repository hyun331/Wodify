package com.soocompany.wodify.common.service;

import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.domain.Role;
import com.soocompany.wodify.member.dto.MemberSaveReqDto;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialCeoDataLoader implements CommandLineRunner {

    @Autowired
    private MemberService memberService;




    @Override
    public void run(String... args) throws Exception {
        if(memberService.isMemberExist("hongildong@naver.com", "N")==null){
            MemberSaveReqDto member = MemberSaveReqDto.builder()
                    .name("홍길동")
                    .email("hongildong@naver.com")
                    .phone("010-1234-5678")
                    .role(Role.CEO)
                    .build();
            memberService.memberRegister(member);
        }



    }
}
