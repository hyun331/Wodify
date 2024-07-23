package com.soocompany.wodify.member.domain;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.common.BaseEntity;
import com.soocompany.wodify.member.dto.MemberDetResDto;
import com.soocompany.wodify.member.dto.MemberListResDto;
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
    private String name;

    @Column(nullable = false)
    private String email;

    private String address;

    @Column(nullable = false)
    private String phone;

    @ColumnDefault("0.0")
    private BigDecimal deadLift;

    @ColumnDefault("0.0")
    private BigDecimal squat;

    @ColumnDefault("0.0")
    private BigDecimal benchPress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "box_id")
    private Box box;


    public MemberDetResDto detFromEntity(){
        //box가 null인거 check -> 안하면 NullPointException
        Long boxId = null;
        String boxName = null;
        if(box!=null){
            boxId = this.box.getId();
            boxName = this.box.getName();
        }
        return MemberDetResDto.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .address(this.address)
                .deadLift(this.deadLift)
                .squat(this.squat)
                .benchPress(this.benchPress)
                .role(this.role)
                .boxId(boxId)
                .boxName(boxName)
                .build();
    }


    public MemberListResDto listFromEntity() {
        return MemberListResDto.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .address(this.address)
                .build();
    }
}
