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


    ///////////////////////////////////
//    @GetMapping("/afterKakaoLogin/{email}")
//    public MemberDetResDto afterLogin(@PathVariable String email){
//        //email 받아옴.. redirect 사용해서 email 받아옴. vue에서 rest인 경우 redirect 처리 필요
//        //email 받아오지 말고 토큰 값 가져와서 email 가져오기!
//        //회원가입한 유저인지 확인해야함. email과 delyn=n인 사람찾기
//        MemberDetResDto memberDetResDto = memberService.isMemberExist(email, "N");
//        if(memberDetResDto !=null){
//            //존재하는 회원 -> home화면으로
//            /////////////////미구현
//            return memberDetResDto;
//        }else{
//            //회원가입 화면으로.
//            //줄 때 email 같이 줘야함
//            /////////////////미구현
//            return null;
//        }
//    }

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<CommonResDto>  memberRegister(@RequestBody MemberSaveReqDto memberSaveReqDto){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "회원가입 완료", memberService.memberRegister(memberSaveReqDto));
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    //코치가 다니는 박스의 회원 리스트
    @PreAuthorize("hasAnyRole('COACH', 'CEO')")
    @GetMapping("/list")
    public ResponseEntity<CommonResDto> nowMemberList(Pageable pageable){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "코치가 다니는 박스의 현재 멤버 리스트", memberService.nowMemberList(pageable));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    //멤버 상세정보
//    @PreAuthorize("hasAnyRole('USER', 'COACH')")
    @GetMapping("/detail")
    public ResponseEntity<CommonResDto> memberDetail(){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "member id에 맞는 멤버 상세 정보 출력", memberService.memberDetail());

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
