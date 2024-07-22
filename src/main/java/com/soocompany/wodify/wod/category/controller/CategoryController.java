package com.soocompany.wodify.wod.category.controller;

import com.soocompany.wodify.wod.category.domain.Category;
import com.soocompany.wodify.wod.category.dto.CategoryListResDto;
import com.soocompany.wodify.wod.category.dto.CategorySaveReqDto;
import com.soocompany.wodify.wod.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wod/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/save")
    public List<Category> categorySave(@RequestBody List<CategorySaveReqDto> dtoList) {
        return categoryService.saveCategory(dtoList);
    }

    @GetMapping("/list")
    @ResponseBody
    public List<CategoryListResDto> categoryList() {
        return categoryService.categoryList();
    }
}