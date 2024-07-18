package com.soocompany.wodify.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private LocalTime time;
//    @JoinColumn("wod_id")
//    @ManyToOne
//    private Wod wod;

//    @JoinColumn("coach_id")
//    @ManyToOne
//    private Member coach;
    @Column(nullable = false)
    private int maximumPeople;
    @Column(nullable = false)
    private int availablePeople;
}
