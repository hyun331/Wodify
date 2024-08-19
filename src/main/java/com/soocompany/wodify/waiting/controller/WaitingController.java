package com.soocompany.wodify.waiting.controller;


import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.waiting.dto.WaitingCreateDto;
import com.soocompany.wodify.waiting.service.WaitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/waiting")
public class WaitingController {

    @Autowired
    private WaitingService waitingService;

    @PostMapping("/add")
    public ResponseEntity<CommonResDto> addToQueue(@RequestBody WaitingCreateDto dto) {
        waitingService.addToQueue(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED,"대기자 명단에 등록되었습니다.",null),HttpStatus.CREATED);
    }

    /**
     * 개발용 api
     */
    @GetMapping("/next")
    public String getNextUser(@RequestParam String reservationId) {
        String userId = waitingService.getNextMember(reservationId);
        return userId != null ? "Next user: " + userId : "Waiting list is empty.";
    }

    @DeleteMapping("/remove")
    public String removeFromQueue(@RequestParam String reservationId,@RequestParam String memberId) {
        waitingService.removeFromQueue(reservationId, memberId);
        return "User removed from waiting list for reservation " + reservationId ;
    }

    @GetMapping("/list")
    public Set<String> getWaitingList(@RequestParam String reservationId) {
        return waitingService.getWaitingList(reservationId);
    }
}
