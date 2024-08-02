package com.soocompany.wodify.box.controller;

import com.soocompany.wodify.box.dto.BoxDetailResDto;
import com.soocompany.wodify.box.dto.BoxListResDto;
import com.soocompany.wodify.box.dto.BoxSaveReqDto;
import com.soocompany.wodify.box.dto.BoxUpdateReqDto;
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
    public ResponseEntity<CommonResDto> boxCreate(@RequestBody BoxSaveReqDto boxSaveReqDto) {
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "Box 생성 완료", boxService.boxCreate(boxSaveReqDto));
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }


    // Box 수정
    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<CommonResDto> boxUpdate(@PathVariable Long id, @RequestBody BoxUpdateReqDto boxUpdateReqDto) {
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Box 수정 완료", boxService.boxUpdate(id, boxUpdateReqDto));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    // Box 삭제
    @PatchMapping("/delete/{id}")
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<CommonResDto> boxDelete(@PathVariable Long id) {
        boxService.boxDelete(id);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "Box 삭제 완료", null);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }



    // Box 목록 조회
    @GetMapping("/list")
    public ResponseEntity<CommonResDto> boxList(Pageable pageable) {
        Page<BoxListResDto> boxes = boxService.boxList(pageable);
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

}
