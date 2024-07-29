package com.soocompany.wodify.box.controller;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.dto.BoxSaveReqDto;
import com.soocompany.wodify.box.dto.BoxUpdateReqDto;
import com.soocompany.wodify.box.service.BoxService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/box")
public class BoxController {

    private final BoxService boxService;
    @Autowired
    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }


    //    box만들기(boxcode는 uniqe)
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> boxCreate(@RequestBody BoxSaveReqDto dto) {
        try {
            Box box = boxService.boxCreate(dto);
            return new ResponseEntity<>(new Response("201", "Box가 성공적으로 생성되었습니다", box), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Response("400", e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }



    //    box수정(box_id, boxcode, 대표id는 못바꿈)
    @PatchMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> boxUpdate(@PathVariable Long id, @RequestBody BoxUpdateReqDto dto) {
        try {
            Box box = boxService.boxUpdate(id, dto);
            return new ResponseEntity<>(new Response("200", "Box가 성공적으로 업데이트되었습니다", box), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Response("404", e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }



    //   box삭제(delyn을 Y로 바꿈)
    @PatchMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> boxDelete(@PathVariable Long id) {
        try {
            boxService.boxDelete(id);
            return new ResponseEntity<>(new Response("200", "Box가 성공적으로 삭제되었습니다", null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Response("404", e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }


//    5개씩 보여지도록 페이지를 만듦
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<?> boxList(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<Box> boxes = boxService.boxList(pageable);
        return new ResponseEntity<>(new Response("200", "Box 목록이 성공적으로 조회되었습니다", boxes), HttpStatus.OK);
    }



    @GetMapping("/detail/{id}")
    @ResponseBody
    public ResponseEntity<?> boxDetail(@PathVariable Long id) {
        try {
            Box box = boxService.boxDetail(id);
            return new ResponseEntity<>(new Response("200", "Box가 성공적으로 조회되었습니다", box), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new Response("404", e.getMessage(), null), HttpStatus.NOT_FOUND);
        }
    }




    // Response 객체 추가
    @Getter
    @AllArgsConstructor
    public static class Response {
        private String code;
        private String message;
        private Object data;
    }
}