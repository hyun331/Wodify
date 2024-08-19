package com.soocompany.wodify.box.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.dto.*;
import com.soocompany.wodify.box.service.BoxService;
import com.soocompany.wodify.common.dto.CommonResDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

@RestController
@RequestMapping("/box")

public class BoxController {

    private final BoxService boxService;

    @Autowired
    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }


    // Box 생성
    @PostMapping("/create")
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<CommonResDto> boxCreate(@ModelAttribute BoxSaveReqDto boxSaveReqDto) {
        // 현재 로그인한 사용자의 ID를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName();

        // representativeId를 로그인한 사용자 ID로 설정
        boxSaveReqDto.setRepresentativeId(Long.parseLong(currentUserId));

        try {
            BoxSaveReqDto responseDto = boxService.boxCreate(boxSaveReqDto);

            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "Box 생성 완료", responseDto);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (Exception e) {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.INTERNAL_SERVER_ERROR, "Box 생성 중 오류 발생: " + e.getMessage(), null);
            return new ResponseEntity<>(commonResDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Box 수정
    @PatchMapping("/update")
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<CommonResDto> boxUpdate(
            @RequestPart("name") String name,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart("operatingHours") String operatingHours,
            @RequestPart("fee") String fee,
            @RequestPart("intro") String intro,
            @RequestPart("address") String address) {

        BoxUpdateReqDto boxUpdateReqDto = BoxUpdateReqDto.builder()
                .name(name)
                .logo(logo)
                .operatingHours(operatingHours)
                .fee(fee)
                .intro(intro)
                .address(address)
                .build();

        try {
            BoxSaveReqDto updatedBoxDto = boxService.boxUpdate(boxUpdateReqDto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Box 수정 완료", updatedBoxDto);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (Exception e) {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.INTERNAL_SERVER_ERROR, "Box 수정 중 오류 발생: " + e.getMessage(), null);
            return new ResponseEntity<>(commonResDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Box 삭제
    @PatchMapping("/delete")
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<CommonResDto> boxDelete() {
        boxService.boxDelete();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Box 삭제 완료", null);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    // Box 목록 조회
    @GetMapping("/list")
    public ResponseEntity<CommonResDto> boxList(
            BoxSearchDto searchDto,
            Pageable pageable) {
        Page<BoxListResDto> boxes = boxService.boxList(searchDto, pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Box 목록이 성공적으로 조회되었습니다", boxes);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    // Box 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<CommonResDto> boxDetail(@PathVariable Long id) {
        try {
            BoxDetailResDto boxDetail = boxService.boxDetail(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Box가 성공적으로 조회되었습니다", boxDetail);

            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            CommonResDto commonResDto = new CommonResDto(HttpStatus.NOT_FOUND, e.getMessage(), null);
            return new ResponseEntity<>(commonResDto, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/name")
    public ResponseEntity<CommonResDto> boxName() {
        String boxName = boxService.boxName();
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Box 이름이 성공적으로 조회되었습니다", boxName);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }
}
