package com.soocompany.wodify.wod.repository;

import com.soocompany.wodify.wod.domain.Wod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WodRepository extends JpaRepository<Wod,Long> {
}
