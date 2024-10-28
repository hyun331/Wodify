package com.soocompany.wodify.registration_info.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResDto {
    private Long id;
    private String memberName;
    private String memberEmail;
    private LocalDate registrationDate;
    private LocalDate endDate;
}
