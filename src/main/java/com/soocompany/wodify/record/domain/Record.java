package com.soocompany.wodify.record.domain;

import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.record.dto.RecordResDto;
import com.soocompany.wodify.record.dto.RecordSaveReqDto;
import com.soocompany.wodify.record.dto.RecordUpdateReqDetDto;
import com.soocompany.wodify.record.dto.RecordUpdateReqDto;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private String snf; // 성공여부 success or failure
    @Column(nullable = false)
    private LocalTime exerciseTime; // 운동 수행 시간
    @Column(length = 3000)
    private String comments; // 코멘트


    @OneToOne // 하나의 예약내역에 하나의 운동기록
    @JoinColumn(name = "reservation_detail_id")
    private ReservationDetail reservationDetail; // FK 예약내역ID

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "record", cascade = CascadeType.PERSIST)
    private List<RecordDetail> recordDetails;


    public void existedRecordUpdateEntity(RecordSaveReqDto dto, LocalTime exerciseTime){
        this.snf = dto.getSnf();
        this.exerciseTime = exerciseTime;
        this.comments = dto.getComments();
        this.updateDelyN(); // delYN을 N으로 바꾼다.
    } // create   // 삭제된거 새로 생성(=수정)할때

    public RecordResDto detFromEntity(){
        return RecordResDto.builder()
                .id(this.id)
                .snf(this.snf).exerciseTime(this.exerciseTime)
                .comments(this.comments)
                .recordResDetDtoList(new ArrayList<>())
                .build();
    } // review

    public void recordUpdateEntity(RecordUpdateReqDto dto, LocalTime exerciseTime){
        this.snf = dto.getSnf();
        this.exerciseTime = exerciseTime;
        this.comments = dto.getComments();
    } // update

    public void recordDelete(){
        this.updateDelYn();
        this.snf = "";
    }

}