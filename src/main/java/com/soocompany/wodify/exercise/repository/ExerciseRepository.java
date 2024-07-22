package com.soocompany.wodify.wod.exercise.repository;

import com.soocompany.wodify.wod.exercise.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByEmailIn(List<String> emails);
    List<Exercise> findByEmailInAndCategoryId(List<String> emails, Long categoryId);

    Optional<List<Exercise>> findByCategoryId(Long categoryId);
}
