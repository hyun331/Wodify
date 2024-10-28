package com.soocompany.wodify.member.dto;

import com.soocompany.wodify.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {
    private String name;
    private String address;
    private String phone;
    private BigDecimal deadLift;
    private BigDecimal squat;
    private BigDecimal benchPress;

}
