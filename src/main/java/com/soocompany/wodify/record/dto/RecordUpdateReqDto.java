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
public class RecordUpdateReqDto { // 앞단에서 가져와 수정하는 값이에용
    private String snf;
    private String exerciseTime;
    private String comments;
    private String visibilityYN;
}