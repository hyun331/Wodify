package com.soocompany.wodify.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationManageEvent {
    private Long reservationId;
    private int people;
}
