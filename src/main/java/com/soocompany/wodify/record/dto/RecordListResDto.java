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
public class RecordListResDto { // 코치가 보는 내용일텐데 어디까지 보여주나
    private String snf;
    private LocalTime exerciseTime;
    private String comments;
}
