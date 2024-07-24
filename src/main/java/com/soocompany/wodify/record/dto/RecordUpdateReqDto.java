package com.soocompany.wodify.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordUpdateReqDto {
    private Long id;
    private String snf;
    private String exerciseTime;
    private String comments;
    private String visibilityYN;
}
