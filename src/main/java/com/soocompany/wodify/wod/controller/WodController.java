package com.soocompany.wodify.wod.controller;

import com.soocompany.wodify.common.exception.CommonResDto;
import com.soocompany.wodify.wod.domain.Wod;
import com.soocompany.wodify.wod.dto.WodResDto;
import com.soocompany.wodify.wod.dto.WodSaveReqDto;
import com.soocompany.wodify.wod.service.WodService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wod")
public class WodController {
    private final WodService wodService;
//    @PreAuthorize("hasRole('COACH')")
    @PostMapping("/save/{memberEmail}")
    public ResponseEntity<?> wodSave(@RequestBody WodSaveReqDto wodSaveReqDto
            , @PathVariable String memberEmail) {
            HttpStatus code = HttpStatus.CREATED;
            String msg = "WOD 등록되었습니다.";
            Long savedId = wodService.wodSave(wodSaveReqDto, memberEmail).getId();
            CommonResDto commonResDto = new CommonResDto(code, msg, savedId);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    @GetMapping("/find")
    public ResponseEntity<?> wodFind(@RequestParam("email") String email
            , @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
            HttpStatus code = HttpStatus.OK;
            String msg = "WOD 찾았습니다.";
            WodResDto wodResDto = wodService.wodFind(email, date);
            CommonResDto commonResDto = new CommonResDto(code, msg, wodResDto);
            return new ResponseEntity<>(commonResDto, code);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/order/cancel")
    public ResponseEntity<?> orderCancel(@RequestParam("email") String email
            , @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        String msg = "WOD 삭제했습니다.";
        HttpStatus code = HttpStatus.OK;
        Wod wod = wodService.wodDelete(email, date);
        return new ResponseEntity<>(new CommonResDto(code, msg, wod.getId()), code);
    }
}
