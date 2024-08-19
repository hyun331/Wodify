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
public class RecordSaveReqDetDto {
    private Long wodDetailId;
    private String detailComments;

    public RecordDetail toEntity(Record record, WodDetail wodDetail) {
        return RecordDetail.builder()
                .record(record)
                .wodDetail(wodDetail)
                .detailComments(this.detailComments)
                .build();
    }
}