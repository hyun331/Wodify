package com.soocompany.wodify.reservation_detail.controller;

import com.soocompany.wodify.reservation_detail.dto.ReservationDetCreateReqDto;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetailDetResDto;
import com.soocompany.wodify.reservation_detail.service.ReservationDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation-detail")
public class ReservationDetailController {
    private final ReservationDetailService reservationDetailService;

    @PostMapping("/create")
    public String reservationDetailCreate(@RequestBody ReservationDetCreateReqDto dto) {
        reservationDetailService.create(dto);
        return "ok";
    }

    @GetMapping("/detail/{id}")
    public ReservationDetailDetResDto reservationDetailDetail(@PathVariable Long id) {
        return reservationDetailService.detail(id);
    }

}