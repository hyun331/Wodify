package com.soocompany.wodify.reservation.controller;

import com.soocompany.wodify.reservation.dto.ReservationCreateReqDto;
import com.soocompany.wodify.reservation.dto.ReservationDetailResDto;
import com.soocompany.wodify.reservation.dto.ReservationListResDto;
import com.soocompany.wodify.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/box/list/{id}")
    public List<ReservationListResDto> reservationList(@PathVariable Long id) {
        return reservationService.reservationList(id);
    }
    @GetMapping("detail/{id}")
    public ReservationDetailResDto reservationDetail(@PathVariable Long id) {
        return reservationService.reservationDetail(id);
    }
}
