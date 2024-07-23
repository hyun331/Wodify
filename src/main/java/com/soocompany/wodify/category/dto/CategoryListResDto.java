package com.soocompany.wodify.wod.category.dto;

import com.soocompany.wodify.wod.category.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListResDto {
    private String name;
}
