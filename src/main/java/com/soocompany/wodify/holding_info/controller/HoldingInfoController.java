package com.soocompany.wodify.holding_info.controller;

import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.holding_info.dto.HoldingInfoListResDto;
import com.soocompany.wodify.holding_info.dto.HoldingInfoResDto;
import com.soocompany.wodify.holding_info.service.HoldingInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HoldingInfoController {
    private final HoldingInfoService holdingInfoService;
    @GetMapping("/holding-info/create")
    public ResponseEntity<CommonResDto> holdingInfoCreate() {
        HoldingInfoResDto resDto = holdingInfoService.holdingInfoCreate();
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "정지정보가 성공적으로 등록되었습니다.", resDto), HttpStatus.CREATED);
    }

    @GetMapping("/holding-info/{id}")
    public ResponseEntity<CommonResDto> holdingInfoList(@PathVariable(value = "id") Long memberId) {
        List<HoldingInfoListResDto> resDto = holdingInfoService.holdingInfoList(memberId);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "정지정보가 성공적으로 조회되었습니다.", resDto), HttpStatus.OK);
    }

    @PatchMapping("/unholding/{id}")
    public ResponseEntity<CommonResDto> unholding(@PathVariable Long id) {
        holdingInfoService.unholding(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "정지가 성공적으로 삭제되었습니다.", null), HttpStatus.OK);
    }

}
