package com.soocompany.wodify.reservation.controller;

import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.reservation.dto.*;
import com.soocompany.wodify.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    @PreAuthorize("hasAnyRole('COACH','CEO')")
    @PostMapping("/create")
    public ResponseEntity<CommonResDto> reservationCreate(@RequestBody List<ReservationCreateReqDto> dtos){
        List<ReservationDetailResDto> detailResDtos = reservationService.reservationCreate(dtos);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"예약 등록 성공",detailResDtos),HttpStatus.OK);
    }

    /**
     * 박스별 전체 예약 목록 조회
     */
    @PreAuthorize("hasAnyRole('COACH','CEO')")
    @GetMapping("/box/list/")
    public ResponseEntity<CommonResDto> reservationList(ReservationSearchDto searchDto, @PageableDefault(sort = "date",direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReservationListResDto> listResDtos = reservationService.reservationList(searchDto, pageable);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"예약 리스트 조회 성공",listResDtos),HttpStatus.OK);
    }
    /**
     * 박스별 날짜별 예약 목록 조회
     */
    @PostMapping("/box/list/")
    public ResponseEntity<CommonResDto> reservationListByDate(@RequestBody ReservationListReqDto dto,@PageableDefault(sort = "date",direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReservationListResDto> listResDtos = reservationService.reservationListByDate(dto,pageable);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"날짜별 예약 리스트 조회 성공",listResDtos),HttpStatus.OK);
    }

    /**
     * 예약 상세 조회
     */
    @PreAuthorize("hasAnyRole('COACH','CEO')")
    @GetMapping("/detail/{id}")
    public ResponseEntity<CommonResDto> reservationDetail(@PathVariable Long id) {
        ReservationDetailResDto resDto = reservationService.reservationDetail(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"예약 조회 성공",resDto),HttpStatus.OK);

    }

    /**
     * 예약 수정
     */
    @PreAuthorize("hasAnyRole('COACH','CEO')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<CommonResDto> reservationUpdate(@PathVariable Long id,@RequestBody ReservationUpdateReqDto dto) {
        ReservationDetailResDto detailResDto = reservationService.reservationUpdate(id,dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"예약 수정 성공",detailResDto),HttpStatus.OK);
    }

    /**
     * 예약 삭제
     */
    @PreAuthorize("hasAnyRole('COACH','CEO')")
    @PatchMapping("/delete/{id}")
    public ResponseEntity<CommonResDto> reservationDelete(@PathVariable Long id) {
        reservationService.reservationDelete(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"삭제 성공",null),HttpStatus.OK);
    }
}
