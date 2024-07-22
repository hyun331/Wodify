package com.soocompany.wodify.reservation.controller;

import com.soocompany.wodify.reservation.dto.ReservationCreateReqDto;
import com.soocompany.wodify.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    @PostMapping("/create")
    public String reservationCreate(@RequestBody ReservationCreateReqDto dto){
        reservationService.reservationCreate(dto);
        return "ok";
    }
}
