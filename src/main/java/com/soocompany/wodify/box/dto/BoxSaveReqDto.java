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
public class BoxSaveReqDto {
    private String name;
    private String code;

    public Box toEntity() {
        return Box.builder()
                .name(this.name)
                .code(this.code)
                .build();
    }
}

