package com.soocompany.wodify.box.controller;

import com.soocompany.wodify.box.dto.BoxSaveReqDto;
import com.soocompany.wodify.box.dto.BoxUpdateReqDto;
import com.soocompany.wodify.box.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
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


    //    box만들기(boxcode, 대표id는 uniqe)
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<String> boxCreate(@RequestBody BoxSaveReqDto dto) {
        try {
            boxService.boxCreate(dto);
            return new ResponseEntity<>("Box created successfully", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    //    box수정(box_id, boxcode, 대표id는 못바꿈)
    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<String> boxUpdate(@PathVariable Long id, @RequestBody BoxUpdateReqDto dto) {
        try {
            boxService.boxUpdate(id, dto);
            return new ResponseEntity<>("Box updated successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    //   box삭제(delyn을 Y로 바꿈)
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> boxDelete(@PathVariable Long id) {
        try {
            boxService.boxDelete(id);
            return new ResponseEntity<>("Box deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}