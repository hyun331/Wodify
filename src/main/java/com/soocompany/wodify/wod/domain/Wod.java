package com.soocompany.wodify.wod.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wod extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "box_id")
    private Box box;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Column(nullable = false)
    private Time timeCap;

    @Column(nullable = false)
    private int rounds;

    @Column(length = 3000)
    private String info;

    @OneToMany(mappedBy = "wod", cascade = CascadeType.PERSIST)
    private List<WodDetail> wodDetails;

    public Wod wodDelete() {
        updateDelYn();
        return this;
    }
}
