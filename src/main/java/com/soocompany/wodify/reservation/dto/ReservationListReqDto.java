package com.soocompany.wodify.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationListReqDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
