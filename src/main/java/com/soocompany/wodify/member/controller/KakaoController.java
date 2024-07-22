package com.soocompany.wodify.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soocompany.wodify.member.domain.OAuthToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class KakaoController {

    @GetMapping("/kakao/login")
    public String loginView(){
        return "kakaoLoginTest";
    }

    @GetMapping("/auth/kakao/callback")
    @ResponseBody
    public String afterLogin(String code) {
        System.out.println(code);
        //Post방식으로 key=value 데이터를 요청
        //이때 필요한 라이브러리가 RestTemplate. http 요청을 용이하게
        RestTemplate rt = new RestTemplate();

        //HttpPost요청할 때 보내는 데이터(body)를 설명해주는 헤더도 같이
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

        //body 데이터를 담을 오브젝트인 MultiValueMap 생성
        //body는 보통 key=value의 쌍으로 이뤄짐. -> java에서 제공하는 MultiValueMap 사용
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "2d129c6af1317e9dc12a8669b1957416");    //Rest API 키
        params.add("redirect_uri", "http://localhost:8081/auth/kakao/callback");
        params.add("code", code);

        //header body 합치기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        //Post방식으로 http요청. response로 응답받기
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token", // https://{요청할 서버 주소}
                HttpMethod.POST, // 요청할 방식
                kakaoTokenRequest, // 요청할 때 보낼 데이터
                String.class // 요청 시 반환되는 데이터 타입
        );

        //response는 json. OAuthToken으로 만들어야함
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try{
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        }catch (JsonMappingException e){
            e.printStackTrace();
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        System.out.println(oAuthToken.getAccess_token());
        ///////////////////////////////////////////

        //member의 이메일 가져오기
        RestTemplate rt2 = new RestTemplate();

        //HttpPost요청할 때 보내는 데이터(body)를 설명해주는 헤더도 같이
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer "+oAuthToken.getAccess_token());
        headers2.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");


        //header body 합치기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);

        //Post방식으로 http요청. response로 응답받기
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me", // https://{요청할 서버 주소}
                HttpMethod.POST, // 요청할 방식
                kakaoProfileRequest2, // 요청할 때 보낼 데이터
                String.class // 요청 시 반환되는 데이터 타입
        );
        System.out.println(response2.getBody());
        JsonNode jsonNode = null;
        String email=null;
        try{
            jsonNode = objectMapper.readTree(response2.getBody());
            email = jsonNode.get("kakao_account").get("email").asText();
        }catch (JsonProcessingException e){
            e.printStackTrace();;
        }
        System.out.println(email);

        return email;

    }



}
