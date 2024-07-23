package com.soocompany.wodify.box.dto;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.member.domain.Member;
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
    private Long representativeId;

    public Box toEntity(Member member) {
        return Box.builder()
                .name(this.name)
                .code(this.code)
                .member(member)
                .build();
    }
}

