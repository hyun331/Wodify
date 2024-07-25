package com.soocompany.wodify.record.dto;

import com.soocompany.wodify.record.domain.Record;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordSaveReqDto { // 앞단에서 받아와 저장하는 값이에용

    private String snf;
    private String exerciseTime; // 앞단에서 어떤 데이터 타입으로 넘어오나? 일단 스트링으로 받는다 치고 서비스에서 변환
    private String comments;
    private String visibilityYN;

    private Long reservationDetailId; // 이거 앞단에서 어떻게 받나..는 나중에 생각해~

    public Record toEntity(ReservationDetail reservationDetail, LocalTime exerciseTime){
        return Record.builder()
                .snf(this.snf).exerciseTime(exerciseTime)
                .comments(this.comments).visibilityYN(this.visibilityYN).reservationDetail(reservationDetail).build();
    }
}