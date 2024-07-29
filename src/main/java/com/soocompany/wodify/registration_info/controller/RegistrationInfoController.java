package com.soocompany.wodify.registration_info.controller;

import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.registration_info.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationInfoController {

    private final RegistrationService registrationService;

    public RegistrationInfoController(RegistrationService registrationService){
        this.registrationService = registrationService;
    }

    //등록하기
    //회원 등록 및 등록 연장(연장시 새로 데이터 추가)
    @PostMapping("/create")
    public ResponseEntity<CommonResDto> registrationCreate(){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "회원 등록 및 등록 연장되었습니다.", registrationService.registrationCreate());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }
}
