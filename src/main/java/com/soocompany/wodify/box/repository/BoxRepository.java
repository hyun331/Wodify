package com.soocompany.wodify.box.repository;

import com.soocompany.wodify.box.domain.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {
}
