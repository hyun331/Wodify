package com.soocompany.wodify.member.dto;


import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSaveReqDto {
    //이메일은 카카오 로그인 api에서 가져와서 form 형식에 value로 넣어둘 예정
    private String email;
    private String name;
    private String address;
    private String phone;
    private BigDecimal deadLift;
    private BigDecimal squat;
    private BigDecimal benchPress;
    private Role role;
    private MultipartFile memberImage;

    public Member toEntity(){
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .address(this.address)
                .phone(this.phone)
                .deadLift(this.deadLift)
                .squat(this.squat)
                .benchPress(this.benchPress)
                .role(this.role)
                .build();

    }

}
