package com.soocompany.wodify.member.domain;

import com.soocompany.wodify.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String address;

    private String phone;

    @ColumnDefault("0.0")
    private BigDecimal deadLift;

    @ColumnDefault("0.0")
    private BigDecimal squat;

    @ColumnDefault("0.0")
    private BigDecimal benchPress;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "box_id")
    private Box box;


}
