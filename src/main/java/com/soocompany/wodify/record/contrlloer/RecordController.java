package com.soocompany.wodify.record.contrlloer;

import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.record.dto.RecordDetResDto;
import com.soocompany.wodify.record.dto.RecordSaveReqDto;
import com.soocompany.wodify.record.dto.RecordUpdateReqDto;
import com.soocompany.wodify.record.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecordController {

    private final RecordService recordService;
    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

//    @PreAuthorize("hasRole('USER')")
    @PutMapping("/record/create") //있으면 수정 // 없으면 등록 딱 putmapping
    public ResponseEntity<Object> recordCreate(@RequestBody RecordSaveReqDto dto){ // dto안에 등록하고자하는 운동기록의 내용과 예약내역 id가 포함되어 있다. 그걸로 예약내역 불러서 운동기록에 넣어야지
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "운동기록이 생성되었습니다.", recordService.recordCreate(dto));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('USER')")
    @GetMapping("/record/detail/{id}")
    public ResponseEntity<Object> recordDetail(@PathVariable Long id){ // 운동기록의 id
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "운동기록 id:"+id+"가 조회되었습니다.", recordService.recordDetail(id));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @GetMapping("/record/list")
    public ResponseEntity<?> memberList(@RequestParam Long memberId, Pageable pageable) {
        Page<RecordDetResDto> dtos = recordService.recordListByMemberId(memberId, pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "운동 기록 리스트가 조회되었습니다.", dtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


//    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/record/update/{id}")
    public ResponseEntity<Object> recordUpdate(@PathVariable Long id, @RequestBody RecordUpdateReqDto dto){  // 운동기록의 id와 운동기록의 수정사항
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "운동기록 id:"+id+"가 수정되었습니다.", recordService.recordUpdate(id,dto));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/record/delete/{id}")
    public ResponseEntity<Object> recordDelete(@PathVariable Long id){ // 운동기록의 id
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "운동기록 id:"+id+"가 삭제되었습니다.", recordService.recordDelete(id));
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }
}