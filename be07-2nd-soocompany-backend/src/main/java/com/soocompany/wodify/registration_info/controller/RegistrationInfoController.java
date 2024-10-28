package com.soocompany.wodify.registration_info.controller;

import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.registration_info.dto.RegistrationSaveReqDto;
import com.soocompany.wodify.registration_info.service.RegistrationService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
public class RegistrationInfoController {

    private final RegistrationService registrationService;

    public RegistrationInfoController(RegistrationService registrationService){
        this.registrationService = registrationService;
    }

    //등록하기
    //회원 등록 및 등록 연장(연장시 새로 데이터 추가)
    @PreAuthorize("hasAnyRole('COACH', 'CEO')")
    @PostMapping("/create")
    public ResponseEntity<CommonResDto> registrationCreate(@RequestBody RegistrationSaveReqDto registrationSaveReqDto){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "회원 등록 및 등록 연장되었습니다.", registrationService.registrationCreate(registrationSaveReqDto));
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    //박스의 등록정보
    @PreAuthorize("hasAnyRole('COACH','CEO')")
    @GetMapping("/list")
    public ResponseEntity<?> registrationList(Pageable pageable){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "박스의 등록정보", registrationService.boxRegistrationList(pageable));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);


    }




}
