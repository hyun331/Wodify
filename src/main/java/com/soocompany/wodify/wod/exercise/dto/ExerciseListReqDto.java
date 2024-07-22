package com.soocompany.wodify.wod.exercise.dto;

import com.soocompany.wodify.wod.category.domain.Category;
import com.soocompany.wodify.wod.exercise.domain.Exercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseListReqDto {
    private String email;
    private Long categoryId;
    private Category category;
}
