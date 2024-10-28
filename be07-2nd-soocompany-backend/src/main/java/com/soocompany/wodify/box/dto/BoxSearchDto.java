package com.soocompany.wodify.box.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoxSearchDto {
    private String searchName;

    private String searchAddress;
}
