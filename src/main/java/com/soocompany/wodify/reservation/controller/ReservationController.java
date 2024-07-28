package com.soocompany.wodify.reservation.controller;

import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.reservation.dto.ReservationCreateReqDto;
import com.soocompany.wodify.reservation.dto.ReservationDetailResDto;
import com.soocompany.wodify.reservation.dto.ReservationListReqDto;
import com.soocompany.wodify.reservation.dto.ReservationListResDto;
import com.soocompany.wodify.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    @PostMapping("/create")
    public ResponseEntity<CommonResDto> reservationCreate(@RequestBody ReservationCreateReqDto dto){
        ReservationDetailResDto detailResDto = reservationService.reservationCreate(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"예약 등록 성공",detailResDto),HttpStatus.OK);
    }

    /**
     * 박스별 전체 예약 목록 조회
     */
    @GetMapping("/box/list/{id}")
    public ResponseEntity<CommonResDto> reservationList(@PathVariable(value = "id") Long boxId, Pageable pageable) {
        Page<ReservationListResDto> listResDtos = reservationService.reservationList(boxId,pageable);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"예약 리스트 조회 성공",listResDtos),HttpStatus.OK);
    }
    /**
     * 박스별 날짜별 예약 목록 조회
     */
    @PostMapping("/box/list/{id}")
    public ResponseEntity<CommonResDto> reservationListByDate(@PathVariable(value = "id") Long boxId, @RequestBody ReservationListReqDto dto, Pageable pageable) {
        Page<ReservationListResDto> listResDtos = reservationService.reservationListByDate(boxId, dto,pageable);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"날짜별 예약 리스트 조회 성공",listResDtos),HttpStatus.OK);
    }

    /**
     * 예약 상세 조회
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<CommonResDto> reservationDetail(@PathVariable Long id) {
        ReservationDetailResDto resDto = reservationService.reservationDetail(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"예약 조회 성공",resDto),HttpStatus.OK);

    }
}