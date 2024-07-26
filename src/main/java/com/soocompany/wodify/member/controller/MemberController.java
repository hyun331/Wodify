package com.soocompany.wodify.member.controller;

import com.soocompany.wodify.common.dto.CommonResDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        //email 받아오지 말고 토큰 값 가져와서 email 가져오기!
        //회원가입한 유저인지 확인해야함. email과 delyn=n인 사람찾기
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
    public ResponseEntity<CommonResDto>  memberRegister(@RequestBody MemberSaveReqDto memberSaveReqDto){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "회원가입 완료", memberService.memberRegister(memberSaveReqDto));
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    //멤버 리스트
    @GetMapping("/list")
    public ResponseEntity<CommonResDto> memberList(Pageable pageable){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "member 전체 리스트 출력", memberService.memberList(pageable));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    //멤버 상세정보
    @GetMapping("/detail/{id}")
    public ResponseEntity<CommonResDto> memberDetail(@PathVariable Long id){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "member id에 맞는 멤버 상세 정보 출력", memberService.memberDetail(id));

        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


        //멤버 개인정보 수정
    @PatchMapping("/update/{id}")
    public ResponseEntity<CommonResDto> memberUpdate(@PathVariable Long id, @RequestBody MemberUpdateDto memberUpdateDto){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "member 정보 수정 완료",memberService.memberUpdate(id, memberUpdateDto));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    //멤버 삭제
    @PatchMapping("/delete/{id}")
    public ResponseEntity<CommonResDto> memberDelete(@PathVariable Long id){
        memberService.memberDelete(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "member 삭제 완료", null);

        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    //코치의 박스 가입 및 변경. 코치 박스 코드 입력 후 submit하면 동작
    @PatchMapping("/coach/box/update/{id}")
    public ResponseEntity<CommonResDto> coachBoxUpdate(@PathVariable Long id, @RequestParam(value = "code")String boxCode){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "코치의 box 수정 성공", memberService.coachBoxUpdate(id, boxCode));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    //박스의 회원 등록하기
    //member의 boxid 변경과 등록정보에 추가 -> 등록정보에서 맡아서 하는게 맞을듯?
//    @PostMapping("/user/box/update/{id}")



    //
    //멤버의 박스 삭제하기
    //박스별 멤버 리스트 조회

}
