package com.soocompany.wodify.record.domain;

import com.soocompany.wodify.common.BaseEntity;
import com.soocompany.wodify.record_detail.domain.RecordDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Record extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id
    @Column(nullable = false)
    private LocalDate exerciseDate; // 운동 날짜
    @Column(nullable = false, columnDefinition = "char(1) default 'N'")
    private String snf; // 성공여부 success or failure
    @Column(nullable = false)
    private LocalTime exerciseTime; // 운동 수행 시간 // TIME 이 맞는 데이터 타입일까? 수행한 시간인데?
    @Column(nullable = false, length = 3000)
    private String comments; // 코멘트
    @Column(nullable = false, columnDefinition = "char(1) default 'Y'")
    private String visibility_YN; // 공개 범위 Y공개, N비공개

//    @OneToOne // 하나의 와드에 하나의 운동기록
//    @JoinColumn(name = "wod_id") // 아직 와드에 대한 걸 못받음.
//    private Wod wod; // FK 와드ID

    @OneToMany(mappedBy = "record") //  하나의 운동기록 안에 여러개의 세부 운동기록 // 순환참조 안걸리게 조심하자
    private List<RecordDetail> recordDetails;
}
