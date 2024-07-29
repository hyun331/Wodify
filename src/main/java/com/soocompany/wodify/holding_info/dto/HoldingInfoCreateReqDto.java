package com.soocompany.wodify.holding_info.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.holding_info.domain.HoldingInfo;
import com.soocompany.wodify.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoldingInfoCreateReqDto {

    private Long memberId;

    private Long boxId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate holdingStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate holdingEnd;

    public HoldingInfo toEntity(Member member, Box box) {
        return HoldingInfo.builder()
                .member(member)
                .box(box)
                .holdingStart(this.holdingStart)
                .holdingEnd(this.holdingEnd)
                .build();
    }
}
