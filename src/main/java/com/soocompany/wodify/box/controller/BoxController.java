package com.soocompany.wodify.box.controller;

import com.soocompany.wodify.box.domain.Box;
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
    public ResponseEntity<CommonResDto> boxCreate(@RequestBody BoxSaveReqDto dto) {
        try {
            Box createdBox = boxService.boxCreate(dto);
            CommonResDto response = new CommonResDto(HttpStatus.CREATED, "Box가 성공적으로 생성되었습니다", createdBox);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            CommonResDto response = new CommonResDto(HttpStatus.BAD_REQUEST, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    // Box 수정
    @PatchMapping("/update/{id}")
    public ResponseEntity<CommonResDto> boxUpdate(@PathVariable Long id, @RequestBody BoxUpdateReqDto dto) {
        try {
            Box updatedBox = boxService.boxUpdate(id, dto);
            CommonResDto response = new CommonResDto(HttpStatus.OK, "Box가 성공적으로 업데이트되었습니다", updatedBox);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            CommonResDto response = new CommonResDto(HttpStatus.NOT_FOUND, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    // Box 삭제
    @PatchMapping("/delete/{id}")
    public ResponseEntity<CommonResDto> boxDelete(@PathVariable Long id) {
        try {
            boxService.boxDelete(id);
            CommonResDto response = new CommonResDto(HttpStatus.OK, "Box가 성공적으로 삭제되었습니다", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            CommonResDto response = new CommonResDto(HttpStatus.NOT_FOUND, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    // Box 목록 조회
    @GetMapping("/list")
    public ResponseEntity<CommonResDto> boxList(Pageable pageable) {
        Page<BoxListResDto> boxes = boxService.boxList(pageable);
        CommonResDto response = new CommonResDto(HttpStatus.OK, "Box 목록이 성공적으로 조회되었습니다", boxes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Box 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<CommonResDto> boxDetail(@PathVariable Long id) {
        try {
            BoxDetailResDto boxDetail = boxService.boxDetail(id);
            CommonResDto response = new CommonResDto(HttpStatus.OK, "Box가 성공적으로 조회되었습니다", boxDetail);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            CommonResDto response = new CommonResDto(HttpStatus.NOT_FOUND, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}
