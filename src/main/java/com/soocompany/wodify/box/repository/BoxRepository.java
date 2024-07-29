package com.soocompany.wodify.box.repository;

import com.soocompany.wodify.box.domain.Box;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {
    Optional<Box> findByIdAndDelYn(Long Id, String delYn);

//    추후 boxcode의 생성방법에 따라 변동.
//    Optional<Box> findByCodeAndDelYn(String code, String delYn);

    Optional<Box> findByMemberIdAndDelYn(Long memberId, String delYn);

    Optional<Box> findByCode(String boxCode);

    Page<Box> findAllByDelYn(String delYn, Pageable pageable);
}

