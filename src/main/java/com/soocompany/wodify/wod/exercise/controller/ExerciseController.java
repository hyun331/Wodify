package com.soocompany.wodify.wod.exercise.controller;

import com.soocompany.wodify.wod.category.domain.Category;
import com.soocompany.wodify.wod.category.dto.CategoryListResDto;
import com.soocompany.wodify.wod.category.dto.CategorySaveReqDto;
import com.soocompany.wodify.wod.exercise.domain.Exercise;
import com.soocompany.wodify.wod.exercise.dto.ExerciseListReqDto;
import com.soocompany.wodify.wod.exercise.dto.ExerciseListResDto;
import com.soocompany.wodify.wod.exercise.dto.ExerciseSaveReqDto;
import com.soocompany.wodify.wod.exercise.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wod/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping("/save")
    public List<Exercise> exerciseSave(@RequestBody List<ExerciseSaveReqDto> dtoList) {
        return exerciseService.saveExercise(dtoList);
    }

    @GetMapping("/list")
    @ResponseBody
    public List<ExerciseListResDto> ExerciseList(@RequestBody ExerciseListReqDto dto) {
        return exerciseService.exerciseList(dto);
    }
}