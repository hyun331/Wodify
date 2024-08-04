package com.soocompany.wodify.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soocompany.wodify.common.auth.JwtTokenProvider;
import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.domain.OAuthToken;
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
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider){
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    ////////////////////////////////////////////////
    //kakao login
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<?> kakaoLogin(String code) {
        RestTemplate rt = new RestTemplate();   //Post방식으로 key=value 데이터를 요청 //이때 필요한 라이브러리가 RestTemplate. http 요청을 용이하게

        //토큰
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "2d129c6af1317e9dc12a8669b1957416");    //Rest API 키
        params.add("redirect_uri", "http://localhost:8090/member/auth/kakao/callback");
        params.add("code", code);

        //header body 합치기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try{
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        }catch (JsonMappingException e){
            e.printStackTrace();
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        System.out.println("tokken");
        System.out.println(oAuthToken+"\n\n\n\n\n");
        ///////////////////////////////////////////

        //member의 이메일 가져오기
        RestTemplate rt2 = new RestTemplate();

        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+oAuthToken.getAccess_token());
        headers2.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class
        );
        System.out.println("body \n"+response2.getBody());
        JsonNode jsonNode = null;
        String email = null;
        try{
            jsonNode = objectMapper.readTree(response2.getBody());
            email = jsonNode.get("kakao_account").get("email").asText();
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }


        //현재 데이터베이스에 있는지 확인
        MemberDetResDto member = memberService.isMemberExist(email, "N");

        if(member!=null){
            //회원으로 존재하면
            String jwtToken = jwtTokenProvider.createToken(member.getId().toString(), member.getRole().toString());

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, member.getName()+"회원님 login 완료", jwtToken);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);

        }else{
            //회원가입 해야함
            CommonResDto commonResDto = new CommonResDto(HttpStatus.UNAUTHORIZED, email+" 이메일이 존재하지 않습니다. 회원가입 필요", email);
            return new ResponseEntity<>(commonResDto, HttpStatus.UNAUTHORIZED);
        }

    }

    //postman 테스트용
    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestParam String email){
        MemberDetResDto member = memberService.isMemberExist(email, "N");
        if(member!=null){
            String jwtToken = jwtTokenProvider.createToken(member.getId().toString(), member.getRole().toString());

            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, member.getName()+"회원님 login 완료", jwtToken);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);

        }else{
            //회원가입 해야함
            CommonResDto commonResDto = new CommonResDto(HttpStatus.UNAUTHORIZED, email+" 이메일이 존재하지 않습니다. 회원가입 필요", email);
            return new ResponseEntity<>(commonResDto, HttpStatus.UNAUTHORIZED);

        }

    }

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<CommonResDto>  memberRegister(@RequestBody MemberSaveReqDto memberSaveReqDto){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "회원가입 완료", memberService.memberRegister(memberSaveReqDto));
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    //박스의 회원 리스트 - 코치, 대표
    @PreAuthorize("hasAnyRole('COACH', 'CEO')")
    @GetMapping("/list/user")
    public ResponseEntity<CommonResDto> boxUserList(Pageable pageable){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "코치가 다니는 박스의 현재 멤버 리스트", memberService.boxUserList(pageable));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    //박스의 코치 리스트 - 대표
    @PreAuthorize("hasRole('CEO')")
    @GetMapping("/list/coach")
    public ResponseEntity<?> boxCoachList(Pageable pageable){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "박스내 코치 리스트", memberService.boxCoachList(pageable));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    //멤버 상세정보
    @GetMapping("/detail")
    public ResponseEntity<CommonResDto> memberDetail(){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "member id에 맞는 멤버 상세 정보 출력", memberService.memberDetail());
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    //멤버 개인정보 수정
    @PatchMapping("/update")
    public ResponseEntity<CommonResDto> memberUpdate(@RequestBody MemberUpdateDto memberUpdateDto){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "member 정보 수정 완료",memberService.memberUpdate(memberUpdateDto));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    //회원 탈퇴
    @PatchMapping("/delete")
    public ResponseEntity<CommonResDto> memberDelete(){
        memberService.memberDelete();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "member 삭제 완료", null);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    //코치의 박스 가입 및 변경. 코치 박스 코드 입력 후 submit하면 동작
    @PreAuthorize("hasRole('COACH')")
    @PatchMapping("/coach/box/update")
    public ResponseEntity<CommonResDto> coachBoxUpdate(@RequestParam(value = "code")String code){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "코치의 box 수정 성공", memberService.coachBoxUpdate(code));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    //박스 회원 탈퇴
    @PreAuthorize("hasRole('CEO')")
    @PatchMapping("/leave/box")
    public ResponseEntity<?> leaveBox(@RequestParam(value = "userEmail") String userEmail){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "박스내 회원 탈퇴", memberService.userLeaveBox(userEmail));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);

    }





    //
    //멤버의 박스 삭제하기
    //박스별 멤버 리스트 조회

}
