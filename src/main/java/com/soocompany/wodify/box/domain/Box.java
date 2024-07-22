package com.soocompany.wodify.box.domain;

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
