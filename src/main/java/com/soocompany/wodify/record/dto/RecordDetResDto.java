package com.soocompany.wodify.record.dto;

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
public class RecordDetResDto {

    private Long id; // id
    private String snf; // 성공여부 success or failure
    private LocalTime exerciseTime; // 운동 수행 시간
    private String comments; // 코멘트
    private String visibilityYN; // 공개 범위 Y공개, N비공개

}
