package com.soocompany.wodify.registration_info.domain;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.registration_info.dto.RegistrationResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_id")
    private Box box;


    //화면에서 등록날짜와 등록할 개월 수를 정하면 endDate는 자동으로 등록
    private LocalDate registrationDate;
    private LocalDate endDate;

    public RegistrationResDto fromEntity(){
        return RegistrationResDto.builder()
                .id(this.id)
                .memberName(this.member.getName())
                .memberEmail(this.member.getEmail())
                .registrationDate(this.registrationDate)
                .endDate(this.endDate)
                .build();
    }
}
