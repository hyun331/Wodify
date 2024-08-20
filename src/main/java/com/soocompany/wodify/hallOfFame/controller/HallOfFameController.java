package com.soocompany.wodify.hallOfFame.controller;

import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.hallOfFame.service.HallOfFameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HallOfFameController {
    private final HallOfFameService hallOfFameService;

    public HallOfFameController(HallOfFameService hallOfFameService) {
        this.hallOfFameService = hallOfFameService;
    }


    @GetMapping("/hallOfFame")
    public ResponseEntity<?> showHallOfFame(){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "명예의 전당", hallOfFameService.getHallOfFame());
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("/hallOfFame/attendance-rate")
    public ResponseEntity<?> showHallOfFameAttendanceRate(){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "명예의 전당 - 출석률", hallOfFameService.getHallOfFameAttendanceRate());
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }
}
