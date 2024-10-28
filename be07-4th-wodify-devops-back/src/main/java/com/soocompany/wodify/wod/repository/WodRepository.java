package com.soocompany.wodify.wod.repository;

import com.soocompany.wodify.wod.domain.Wod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WodRepository extends JpaRepository<Wod, Long> {
    Optional<Wod> findByBoxIdAndDateAndDelYn(Long boxId, LocalDate date, String DelYn);
}