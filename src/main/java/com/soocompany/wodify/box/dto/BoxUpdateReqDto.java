package com.soocompany.wodify.box.dto;

import com.soocompany.wodify.box.domain.Box;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoxUpdateReqDto {
    private String name;

    private String logo;

    private String operatingHours;

    private String fee;

    private String intro;

    private String address;

}