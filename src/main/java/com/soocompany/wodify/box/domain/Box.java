package com.soocompany.wodify.box.domain;

import com.soocompany.wodify.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import lombok.*;
import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Box extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String logo;

    private String operatingHours;

    private String fee;

    private String intro;

    private String address;

    @Column(unique = true)
    private String code;

    @JoinColumn(name = "representative_id")
    @OneToOne
    private Member member;



    //    수정할 때 null값을 넣으면 그 값은 그냥 원래 값 그대로 저장
    public void updateDetails(String name, String logo, String operatingHours, String fee, String intro, String address) {
        if (name != null) this.name = name;
        if (logo != null) this.logo = logo;
        if (operatingHours != null) this.operatingHours = operatingHours;
        if (fee != null) this.fee = fee;
        if (intro != null) this.intro = intro;
        if (address != null) this.address = address;
    }


//    UUID생성
    public Box(String name, String logo, String operatingHours, String fee, String intro, String address, Member member) {
        this.name = name;
        this.logo = logo;
        this.operatingHours = operatingHours;
        this.fee = fee;
        this.intro = intro;
        this.address = address;
        this.code = UUID.randomUUID().toString(); // boxcode의 UUID 생성
        this.member = member;
    }

}