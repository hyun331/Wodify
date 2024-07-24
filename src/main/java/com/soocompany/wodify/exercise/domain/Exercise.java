package com.soocompany.wodify.wod.exercise.domain;

import com.soocompany.wodify.common.BaseEntity;
import com.soocompany.wodify.wod.category.domain.Category;
import com.soocompany.wodify.wod.exercise.dto.ExerciseListResDto;
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
public class Exercise extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Character reps;

    @Column(nullable = false)
    private Character kg;

    @Column(nullable = false)
    private Character meter;

    @Column(nullable = false)
    private Character cal;

    @Column(nullable = false)
    private Character sec;

    public ExerciseListResDto toDto() {
        return ExerciseListResDto.builder()
                .name(this.name)
                .build();
    }
}
