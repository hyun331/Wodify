package com.soocompany.wodify.holding_info.domain;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.holding_info.dto.HoldingInfoListResDto;
import com.soocompany.wodify.holding_info.dto.HoldingInfoResDto;
import com.soocompany.wodify.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HoldingInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

    @ManyToOne
    @JoinColumn(name = "box_id")
    private Box box;

    private LocalDate holdingStart;
    private LocalDate holdingEnd;


    public HoldingInfoResDto fromEntity() {
        return HoldingInfoResDto.builder()
                .id(this.id)
                .memberId(this.member.getId())
                .memberName(this.member.getName())
                .memberEmail(this.member.getEmail())
                .holdingStart(this.holdingStart)
                .holdingEnd(this.holdingEnd)
                .build();
    }

    public HoldingInfoListResDto ListfromEntity() {
        return HoldingInfoListResDto.builder()
                .id(this.id)
                .holdingStart(this.holdingStart)
                .holdingEnd(this.holdingEnd).build();
    }
}
