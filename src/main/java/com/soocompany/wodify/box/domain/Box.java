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

    @Column(nullable = false, unique = true)
    private String code;

    @JoinColumn(name = "representative_id", unique = true)
    @OneToOne
    private Member member;



    //    수정할 때 null값을 넣으면 그 값은 그냥 원래 값 그대로 저장
    public void updateDetails(String name, String logo, String operatingHours, String fee, String intro) {
        if (name != null) this.name = name;
        if (logo != null) this.logo = logo;
        if (operatingHours != null) this.operatingHours = operatingHours;
        if (fee != null) this.fee = fee;
        if (intro != null) this.intro = intro;
    }


    public void markAsDeleted() {
        try {
            java.lang.reflect.Field field = BaseEntity.class.getDeclaredField("delYn");
            field.setAccessible(true);
            field.set(this, "Y");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to mark box as deleted", e);
        }
    }

}