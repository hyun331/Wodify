package com.soocompany.wodify.wod.category.domain;

import com.soocompany.wodify.common.BaseEntity;
import com.soocompany.wodify.wod.category.dto.CategoryListResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public CategoryListResDto toDto() {
        return CategoryListResDto.builder()
                .name(this.name)
                .build();
    }
}
