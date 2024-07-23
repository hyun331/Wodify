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

    @Lob
    private byte[] logo;

    private String operatingHours;

    private String fee;

    private String intro;

    @Column(nullable = false)
    private String code;

    @JoinColumn(name = "representative_id")
    @OneToOne
    private Member member;


    public void setName(String name) {
        this.name = name;
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
