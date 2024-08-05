package com.soocompany.wodify.member.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//박스의 회원관리 리스트
public class MemberManagementListDto{
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private LocalDate registrationDate;
    private LocalDate endDate;
}
