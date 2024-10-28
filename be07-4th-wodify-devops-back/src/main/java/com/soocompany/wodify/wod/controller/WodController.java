package com.soocompany.wodify.wod.controller;
import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.wod.domain.Wod;
import com.soocompany.wodify.wod.dto.WodDetResDto;
import com.soocompany.wodify.wod.dto.WodResDto;
import com.soocompany.wodify.wod.dto.WodSaveReqDto;
import com.soocompany.wodify.wod.service.WodService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
@RestController
@RequiredArgsConstructor
@RequestMapping("/wod")
public class WodController {
    private final WodService wodService;
//    @PreAuthorize("hasRole('COACH')")
    @PostMapping("/save")
    public ResponseEntity<?> wodSave(@RequestBody WodSaveReqDto wodSaveReqDto) {
        HttpStatus code = HttpStatus.CREATED;
        String msg = "WOD 를 등록했습니다.";
        Long savedId = wodService.wodSave(wodSaveReqDto).getId();
        CommonResDto commonResDto = new CommonResDto(code, msg, savedId);
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    @GetMapping("/find/{date}")
    public ResponseEntity<?> wodFind(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        HttpStatus code = HttpStatus.OK;
        String msg = "WOD 를 찾았습니다.";
        WodResDto wodResDto = wodService.wodFind(date);
        CommonResDto commonResDto = new CommonResDto(code, msg, wodResDto);
        return new ResponseEntity<>(commonResDto, code);
    }

//    @PreAuthorize("hasRole('COACH')")
    @PatchMapping("/delete/{date}")
    public ResponseEntity<?> wodDelete(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        String msg = "WOD 를 삭제했습니다.";
        HttpStatus code = HttpStatus.OK;
        Wod wod = wodService.wodDelete(date);
        return new ResponseEntity<>(new CommonResDto(code, msg, wod.getId()), code);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> wodSearchById(@PathVariable String id){
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "id에 따른 wod 정보", wodService.wodSearchById(Long.parseLong(id))), HttpStatus.OK);

    }

    @GetMapping("/random/{id}")
    public ResponseEntity<?> randomWodDet(@PathVariable Long id) {
        String msg = "Count 반환에 성공했습니다.";
        HttpStatus code = HttpStatus.OK;
        WodDetResDto wodDetResDto = wodService.randomWodDet(id);
        return new ResponseEntity<>(new CommonResDto(code, msg, wodDetResDto), code);
    }

    @GetMapping("/count")
    public ResponseEntity<?> wodDetCount() {
        String msg = "Count 반환에 성공했습니다.";
        HttpStatus code = HttpStatus.OK;
        Long count = wodService.wodDetCount();
        return new ResponseEntity<>(new CommonResDto(code, msg, count), code);
    }
}
