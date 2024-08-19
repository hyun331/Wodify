package com.soocompany.wodify.record.domain;

import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.record.dto.RecordResDetDto;
import com.soocompany.wodify.wod.domain.WodDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id

    @ManyToOne
    @JoinColumn(name = "Record_id") // 하나의 운동기록에 여러개의 기록디테일
    private Record record;

    @ManyToOne
    @JoinColumn(name = "WodDetail_id") // 하나의 와드 디테일에 여러개의 기록디테일
    private WodDetail wodDetail;

    @Column(length = 3000)
    private String detailComments;

    public RecordResDetDto fromEntity(String name, String content){
        return RecordResDetDto.builder()
                .id(this.id)
                .wodName(name)
                .wodContents(content)
                .detailComments(this.detailComments)
                .build();
    }

    public void recordDetailUpdateEntity(String detailComment){
        this.detailComments = detailComment;
    } // update
}
