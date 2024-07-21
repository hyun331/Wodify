package com.soocompany.wodify.wod.exercise.dto;

import com.soocompany.wodify.wod.category.domain.Category;
import com.soocompany.wodify.wod.exercise.domain.Exercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseSaveReqDto {
    private String email;
    private String name;
    private Long categoryId;
    private Category category;
    private Character reps;
    private Character kg;
    private Character meter;
    private Character cal;
    private Character sec;

    public Exercise toEntity() {
        return Exercise.builder()
                .email(this.email)
                .name(this.name)
                .category(this.category)
                .reps(this.reps)
                .kg(this.kg)
                .meter(this.meter)
                .cal(this.cal)
                .sec(this.sec)
                .build();
    }
}
