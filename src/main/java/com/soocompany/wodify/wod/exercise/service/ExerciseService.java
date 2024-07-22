package com.soocompany.wodify.wod.exercise.service;

import com.soocompany.wodify.wod.category.domain.Category;
import com.soocompany.wodify.wod.category.dto.CategoryListResDto;
import com.soocompany.wodify.wod.category.dto.CategorySaveReqDto;
import com.soocompany.wodify.wod.category.repository.CategoryRepository;
import com.soocompany.wodify.wod.exercise.domain.Exercise;
import com.soocompany.wodify.wod.exercise.dto.ExerciseListReqDto;
import com.soocompany.wodify.wod.exercise.dto.ExerciseListResDto;
import com.soocompany.wodify.wod.exercise.dto.ExerciseSaveReqDto;
import com.soocompany.wodify.wod.exercise.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, CategoryRepository categoryRepository) {
        this.exerciseRepository = exerciseRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Exercise> saveExercise(List<ExerciseSaveReqDto> dtoList) {
        List<Exercise> savedExerciseList = new ArrayList<>();
        for (ExerciseSaveReqDto dto : dtoList) {
            dto.setCategory(categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("id에 해당하는 카테고리가 없습니다.")));
            savedExerciseList.add(exerciseRepository.save(dto.toEntity()));
        }
        return savedExerciseList;
    }

    public List<ExerciseListResDto> exerciseList(ExerciseListReqDto dto) {
        return exerciseRepository
                .findByCategoryId(dto.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("카테고리 id에 해당되는 종목이 없습니다."))
                .stream()
                .map(Exercise::toDto)
                .collect(Collectors.toList());
    }

    public List<ExerciseListResDto> exerciseListAllByEmail(ExerciseListReqDto dto) {
        return exerciseRepository
                .findByEmailIn(Arrays.asList("admin@naver.com", "coach@naver.com", dto.getEmail()))
                .stream()
                .map(Exercise::toDto)
                .collect(Collectors.toList());
    }

    public List<ExerciseListResDto> exerciseListByEmailAndCategoryId(ExerciseListReqDto dto) {
        return exerciseRepository
                .findByEmailInAndCategoryId(Arrays.asList("admin@naver.com", "coach@naver.com", dto.getEmail()), dto.getCategoryId())
                .stream()
                .map(Exercise::toDto)
                .collect(Collectors.toList());
    }
}
