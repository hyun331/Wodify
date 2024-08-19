package com.soocompany.wodify.record.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordResDetDto {
    private Long id;
    private String wodName;
    private String wodContents;
    private String detailComments;

}
