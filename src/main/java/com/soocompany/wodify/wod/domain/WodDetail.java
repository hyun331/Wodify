package com.soocompany.wodify.wod.domain;

import com.soocompany.wodify.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WodDetail extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wod_id")
    private Wod wod;

    @Column(nullable = false)
    private String name;

    @Column(length = 3000)
    private String contents;
}
