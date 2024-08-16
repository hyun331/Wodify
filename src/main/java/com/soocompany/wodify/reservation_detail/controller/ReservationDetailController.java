package com.soocompany.wodify.reservation_detail.controller;

import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.reservation.dto.ReservationSearchDto;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetCreateReqDto;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetailDetResDto;
import com.soocompany.wodify.reservation_detail.service.ReservationDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation-detail")
public class ReservationDetailController {
    private final ReservationDetailService reservationDetailService;

    @PostMapping("/create")
    public ResponseEntity<CommonResDto> reservationDetailCreate(@RequestBody ReservationDetCreateReqDto dto) {
        ReservationDetailDetResDto detailResDto = reservationDetailService.reservationCreate(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED,"예약상세 등록 성공",detailResDto),HttpStatus.CREATED);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<CommonResDto> reservationDetailDetail(@PathVariable Long id) {
        ReservationDetailDetResDto detResDto = reservationDetailService.detail(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"예약싱세 조회 성공",detResDto),HttpStatus.OK);
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<CommonResDto> reservationDetailListByMember(@PathVariable Long id, Pageable pageable) {
        Page<ReservationDetailDetResDto> detResDtoList = reservationDetailService.listByMember(id, pageable);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"예약싱세 조회 성공",detResDtoList),HttpStatus.OK);
    }
    @GetMapping("/mylist")
    public ResponseEntity<CommonResDto> reservationDetail(ReservationSearchDto searchDto, Pageable pageable) {
        Page<ReservationDetailDetResDto> detResDtoList = reservationDetailService.myReservationList(searchDto, pageable);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"예약싱세 조회 성공",detResDtoList),HttpStatus.OK);
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<CommonResDto> reservationDetailDelete(@PathVariable Long id) {
        reservationDetailService.delete(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"예약싱세 삭제 성공",null),HttpStatus.OK);
    }

}