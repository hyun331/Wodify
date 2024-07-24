package com.soocompany.wodify.member.controller;

import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.dto.MemberDetResDto;
import com.soocompany.wodify.member.dto.MemberListResDto;
import com.soocompany.wodify.member.dto.MemberSaveReqDto;
import com.soocompany.wodify.member.dto.MemberUpdateDto;
import com.soocompany.wodify.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @GetMapping("/afterKakaoLogin/{email}")
    public MemberDetResDto afterLogin(@PathVariable String email){
        //email 받아옴.. redirect 사용해서 email 받아옴. vue에서 rest인 경우 redirect 처리 필요

        //회원가입한 유저인지 확인해야함. email과 delyn=n인 사람찾기
        System.out.println(email+"\n\n\n\n\n\n\n");
        MemberDetResDto memberDetResDto = memberService.isMemberExist(email, "N");
        if(memberDetResDto !=null){
            //존재하는 회원 -> home화면으로
            /////////////////미구현
            return memberDetResDto;
        }else{
            //회원가입 화면으로.
            //줄 때 email 같이 줘야함
            /////////////////미구현
            return null;
        }
    }


    //회원가입
    @PostMapping("/register")
    public MemberDetResDto memberRegister(@RequestBody MemberSaveReqDto memberSaveReqDto){
        return memberService.memberRegister(memberSaveReqDto);
    }

    //멤버 리스트
    @GetMapping("/list")
    public Page<MemberListResDto> memberList(@PageableDefault(size=10, sort="createdTime", direction = Sort.Direction.ASC) Pageable pageable){
        return memberService.memberList(pageable);
    }

    //멤버 상세정보
    @GetMapping("/detail/{id}")
    public MemberDetResDto memberDetail(@PathVariable Long id){
        return memberService.memberDetail(id);
    }

    @PostMapping("/update/{id}")
    public MemberDetResDto memberUpdate(@PathVariable Long id, @RequestBody MemberUpdateDto memberUpdateDto){
        return memberService.memberUpdate(id, memberUpdateDto);
    }


}
