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
public class RecordSaveReqDto {

    private String snf; // 성공여부 success or failure
    private String exerciseTime; // 운동 수행 시간 // 앞단에서 어떤 데이터 타입으로 넘어오나? 일단 스프링으로 받는다 치고 서비스에서 변환
    private String comments; // 코멘트
    private String visibilityYN; // 공개 범위 Y공개, N비공개

    private Long reservationDetailId; // 이거 앞단에서 어떻게 받나..

    public Record toEntity(LocalTime exerciseTime){
        return Record.builder()
                .snf(this.snf).exerciseTime(exerciseTime)
                .comments(this.comments).visibilityYN(this.visibilityYN).build();
    }
}
