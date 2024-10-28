package com.soocompany.wodify.member.domain;


import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.member.dto.MemberDetResDto;
import com.soocompany.wodify.member.dto.MemberListResDto;
import com.soocompany.wodify.member.dto.MemberManagementListDto;
import com.soocompany.wodify.member.dto.MemberUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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


    //fetch join으로 추후에 변경하기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_id")
    private Box box;


    private String imagePath;


    public MemberDetResDto detFromEntity(){
        //box가 null인거 check -> 안하면 NullPointException
        Long boxId = null;
        String boxName = null;
        if(this.box!=null){
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
                .imagePath(this.imagePath)
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


    public MemberManagementListDto managementListFromEntity(LocalDate registrationDate, LocalDate endDate) {
        return MemberManagementListDto.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .address(this.address)
                .registrationDate(registrationDate)
                .endDate(endDate)
                .build();
    }


    //member 개인정보 수정
    public void memberInfoUpdate(MemberUpdateDto memberUpdateDto) {
        this.name = memberUpdateDto.getName();
        this.address = memberUpdateDto.getAddress();
        this.phone = memberUpdateDto.getPhone();
        this.deadLift = memberUpdateDto.getDeadLift();
        this.squat = memberUpdateDto.getSquat();
        this.benchPress = memberUpdateDto.getBenchPress();
    }


    //member box 수정
    public void memberBoxUpdate(Box box){
        this.box = box;
    }


    public void updateImagePath(String s3Path) {
        this.imagePath = s3Path;
    }
}
