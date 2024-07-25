package com.soocompany.wodify.box.repository;

import com.soocompany.wodify.box.domain.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {
    Optional<Box> findByIdAndDelYn(Long Id, String delYn);

    Optional<Box> findByCode(String code);

    Optional<Box> findByMember_Id(Long memberId);
}