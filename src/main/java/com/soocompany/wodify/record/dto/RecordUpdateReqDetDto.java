package com.soocompany.wodify.record.dto;

import com.soocompany.wodify.record.domain.Record;
import com.soocompany.wodify.record.domain.RecordDetail;
import com.soocompany.wodify.wod.domain.WodDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordUpdateReqDetDto {
    private Long wodDetailId;
    private String detailComments;
}
