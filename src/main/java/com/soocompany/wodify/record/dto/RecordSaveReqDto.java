package com.soocompany.wodify.record.dto;

import com.soocompany.wodify.record.domain.Record;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordSaveReqDto { // 앞단에서 받아와 저장하는 값이에용

    private String snf;
    private String exerciseTime; // 앞단에서 어떤 데이터 타입으로 넘어오나? 일단 스트링으로 받는다 치고 서비스에서 변환
    private String comments;

    private Long reservationDetailId; // 이거 앞단에서 받겠징

    private List<RecordSaveReqDetDto> recordSaveReqDetDtoList;


    public Record toEntity(ReservationDetail reservationDetail, LocalTime exerciseTime){
        return Record.builder()
                .snf(this.snf).exerciseTime(exerciseTime)
                .comments(this.comments)
                .reservationDetail(reservationDetail)
                .recordDetails(new ArrayList<>()).build();

    }
}