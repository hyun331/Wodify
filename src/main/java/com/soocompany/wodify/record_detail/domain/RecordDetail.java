//package com.soocompany.wodify.record_detail.domain;
//
//import com.soocompany.wodify.common.domain.BaseEntity;
//import com.soocompany.wodify.record.domain.Record;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.math.BigDecimal;
//import java.time.LocalTime;
//
//@Getter
//@Entity
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class RecordDetail extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @Column(nullable = false)
//    private Integer rounds; // 라운드
//    private Integer reps; // 횟수
//    private BigDecimal kg; // 무게
//    private BigDecimal meter; // 거리
//    private Integer cal; // 칼로리
//    private LocalTime sec; // 시간
//
//    @ManyToOne // 하나의 운동기록 안에 여러개의 세부 운동기록
//    @JoinColumn(name = "record_id")
//    private Record record; // FK 운동기록ID
//
//
//}