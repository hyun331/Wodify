package com.soocompany.wodify.hallOfFame.domain;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.hallOfFame.dto.HallOfFameResDto;
import com.soocompany.wodify.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HallOfFame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_id")
    private Box box;

    private int rank;

    private BigDecimal deadLift;
    private BigDecimal squat;
    private BigDecimal benchPress;
    private BigDecimal total;


    public HallOfFameResDto fromEntity(){
        return HallOfFameResDto.builder()
                .id(this.id)
                .rank(this.rank)
                .name(this.member.getName())
                .email(this.member.getEmail())
                .deadLift(this.deadLift)
                .squat(this.squat)
                .benchPress(this.benchPress)
                .total(this.total)
                .build();

    }
}
