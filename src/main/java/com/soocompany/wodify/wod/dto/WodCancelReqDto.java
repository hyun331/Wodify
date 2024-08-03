package com.soocompany.wodify.wod.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WodCancelReqDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}