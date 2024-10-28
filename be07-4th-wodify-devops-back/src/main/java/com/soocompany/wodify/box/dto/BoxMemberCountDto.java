package com.soocompany.wodify.box.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoxMemberCountDto {
    private Long Id;
    private String name;
    private String info;
    private String logoPath;
    private Long memberCount;
}
