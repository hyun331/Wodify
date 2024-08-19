package com.soocompany.wodify.wod.repository;

import com.soocompany.wodify.wod.domain.WodDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WodDetailRepository extends JpaRepository<WodDetail, Long> {
    Optional<WodDetail> findByIdAndDelYn(Long id, String DelYn);
}
