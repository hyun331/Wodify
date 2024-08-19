package com.soocompany.wodify.post.dto;
import com.soocompany.wodify.record.domain.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRecordResDto {
    private Time time;
    private Character snf;
    private String comments;
}
