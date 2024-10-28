package com.soocompany.wodify.registration_info.dto;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.registration_info.domain.RegistrationInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.Registration;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationSaveReqDto {
    //등록할 회원 email
    private String email;
    private LocalDate registrationDate;
    //등록할 개월 수
    private int registrationMonth;

    public RegistrationInfo toEntity(Member userMember, Box box, LocalDate endDate) {
        return RegistrationInfo.builder()
                .member(userMember)
                .box(box)
                .registrationDate(this.registrationDate)
                .endDate(endDate)
                .build();
    }
}
