package com.soocompany.wodify.wod.repository;

import com.soocompany.wodify.wod.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
