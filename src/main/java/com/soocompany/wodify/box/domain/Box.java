package com.soocompany.wodify.box.domain;


import com.soocompany.wodify.common.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import lombok.*;
import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Box {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob
    private byte[] logo;

    private String operatingHours;

    private String fee;

    private String intro;

    @Column(nullable = false)
    private String code;

    @JoinColumn(name = "representative_id")
    @ManyToOne
    private Member representative;

}
