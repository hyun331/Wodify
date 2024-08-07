package com.soocompany.wodify.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordDetResDto { // 뒷단에서 가져와 보여주는 값이에용
    private String snf; // 성공여부 success or failure
    private LocalTime exerciseTime; // 운동 수행 시간
    private String comments; // 코멘트
}