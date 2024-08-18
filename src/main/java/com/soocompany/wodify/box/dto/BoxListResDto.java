package com.soocompany.wodify.box.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoxListResDto {
    private Long id;
    private String logoPath;
    private String name;
    private String address;
    private String operatingHours;
    private String fee;
}
