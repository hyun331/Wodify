package com.soocompany.wodify.wod.exercise.controller;

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

    @GetMapping("/list/all")
    @ResponseBody
    public List<ExerciseListResDto> exerciseListAllByEmail(@RequestBody ExerciseListReqDto dto) {
        return exerciseService.exerciseListAllByEmail(dto);
    }

    @GetMapping("/list/categoryId")
    @ResponseBody
    public List<ExerciseListResDto> exerciseListByEmailAndCategoryId(@RequestBody ExerciseListReqDto dto) {
        return exerciseService.exerciseListByEmailAndCategoryId(dto);
    }
}