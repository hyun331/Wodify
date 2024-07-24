package com.soocompany.wodify.wod.category.service;

import com.soocompany.wodify.wod.category.domain.Category;
import com.soocompany.wodify.wod.category.dto.CategoryListResDto;
import com.soocompany.wodify.wod.category.dto.CategorySaveReqDto;
import com.soocompany.wodify.wod.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> saveCategory(List<CategorySaveReqDto> dtoList) {
        List<Category> savedCategoryList = new ArrayList<>();
        for (CategorySaveReqDto dto : dtoList) {
            savedCategoryList.add(categoryRepository.save(dto.toEntity()));
        }
        return savedCategoryList;
    }

    public List<CategoryListResDto> categoryList() {
        return categoryRepository
                .findAll()
                .stream()
                .map(Category::toDto)
                .collect(Collectors.toList());
    }
}
