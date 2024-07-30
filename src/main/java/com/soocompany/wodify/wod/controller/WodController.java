package com.soocompany.wodify.wod.controller;

import com.soocompany.wodify.common.exception.CommonErrorDto;
import com.soocompany.wodify.common.exception.CommonResDto;
import com.soocompany.wodify.wod.dto.WodSaveReqDto;
import com.soocompany.wodify.wod.service.WodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wod")
public class WodController {
    private final WodService wodService;
//    @PreAuthorize("hasRole('COACH')")
    @PostMapping("/save/{email}")
    public ResponseEntity<?> wodSave(@RequestBody WodSaveReqDto wodSaveReqDto, @PathVariable String email) {
        try {
            HttpStatus code = HttpStatus.CREATED;
            String msg = "Order is successfully created";
            Long savedId = wodService.wodSave(wodSaveReqDto, email).getId();
            CommonResDto commonResDto = new CommonResDto(code, msg, savedId);
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            HttpStatus code = HttpStatus.BAD_REQUEST;
            String msg = "BAD REQUEST";
            CommonErrorDto commonErrorDto = new CommonErrorDto(code, msg);
            return new ResponseEntity<>(commonErrorDto, code);
        }
    }
//
//    @GetMapping("/find/{date}")
//    public ResponseEntity<?> wodFind(@PathVariable Date date) {
//        try {
//            HttpStatus code = HttpStatus.OK;
//            String msg = "Wod is successfully found";
//            WodResDto wodResDto = wodService.wodFind(date);
//            CommonResDto commonResDto = new CommonResDto(code, msg, wodResDto);
//            return new ResponseEntity<>(commonResDto, code);
//        } catch (IllegalArgumentException e) {
//            HttpStatus code = HttpStatus.BAD_REQUEST;
//            String msg = "BAD REQUEST";
//            CommonErrorDto commonErrorDto = new CommonErrorDto(code, msg);
//            return new ResponseEntity<>(commonErrorDto, code);
//        }
//    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PatchMapping("/order/{id}/cancel")
//    public ResponseEntity<?> orderCancel(@PathVariable Long id) {
//        try {
//            return new ResponseEntity<>(
//                    new CommonResDto(HttpStatus.OK
//                            , "Order is successfully canceled"
//                            , orderService.orderCancel(id))
//                    , HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(new CommonErrorDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
//        }
//    }
}
