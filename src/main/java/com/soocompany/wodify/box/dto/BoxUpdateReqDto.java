package com.soocompany.wodify.box.dto;

import com.soocompany.wodify.box.domain.Box;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoxUpdateReqDto {
    private String name;

    private MultipartFile logo;

    private String operatingHours;

    private String fee;

    private String intro;

    private String address;

}