package com.soocompany.wodify.record_detail.domain;

import com.soocompany.wodify.common.BaseEntity;
import com.soocompany.wodify.record.domain.Record;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer rounds; // 라운드 // tinyint 하고 싶다. 100이 안넘어가잖아
    private Integer reps; // 횟수 // tinyint 하고 싶다. 100이 안넘어가잖아
    private BigDecimal kg; // 무게
    private BigDecimal meter; // 거리
    private Integer cal; // 칼로리 // 얘는 100 넘어가겠다.
    private LocalTime sec; // 시간 // TIME 이 맞는 데이터 타입일까? 수행한 시간인데? 강사님 왈 시간의 양이면 숫자값을 받으라는디?

    @ManyToOne // 하나의 운동기록 안에 여러개의 세부 운동기록
    @JoinColumn(name = "record_id")
    private Record record; // FK 운동기록ID

//    @ManyToOne // 하나의 종목에 대해 개개인들의 세부 운동기록?   // 이거 헷갈린다...
//    @JoinColumn(name = "exercise_id")  // 아직 와드+종목에 대한 걸 못받음.
//    private Exercise exercise; // FK 종목ID
//    여전히 의문인것.. 사실은 운동기록에 있는 와드id를 가지고 와드 디테일에 가서 종목을 가져오든 해야하는거 아닌가 복잡하긴 해도..
//    그냥 종목을 바로 가져와서 기록한다고 하면, 와드에서 한 운동이 아니라 아무 종목이나 가능한거 아니야?
//    가지고 있는 운동 기록에 있는 와드id에서 와드 디테일에서 종목id를 가져와서 비교해...? 그럼 위 방법이랑 뭐가 달라..?

}
