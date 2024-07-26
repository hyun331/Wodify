package com.soocompany.wodify.record.domain;

import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.record.dto.RecordDetResDto;
import com.soocompany.wodify.record.dto.RecordUpdateReqDto;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Record extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id
    @Column(nullable = false, columnDefinition = "char(1) default 'N'")
    private String snf; // 성공여부 success or failure
    @Column(nullable = false)
    private LocalTime exerciseTime; // 운동 수행 시간
    @Column(length = 3000)
    private String comments; // 코멘트
    @Column(nullable = false, columnDefinition = "char(1) default 'Y'")
    private String visibilityYN; // 공개 범위 Y공개, N비공개


    @OneToOne // 하나의 예약내역에 하나의 운동기록
    @JoinColumn(name = "reservation_detail_id")
    private ReservationDetail reservationDetail; // FK 예약내역ID


    public RecordDetResDto detFromEntity(){
        return RecordDetResDto.builder()
                .snf(this.snf).exerciseTime(this.exerciseTime)
                .comments(this.comments).visibilityYN(this.visibilityYN).build();
    }

    public void recordUpdateEntity(RecordUpdateReqDto dto){
        this.snf = dto.getSnf();
        this.exerciseTime = dto.getExerciseTime();
        this.comments = dto.getComments();
        this.visibilityYN = dto.getVisibilityYN();
    }

}