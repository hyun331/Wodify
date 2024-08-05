package com.soocompany.wodify.record.repository;

import com.soocompany.wodify.record.domain.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecordReository extends JpaRepository<Record, Long> {
    Optional<Record> findByReservationDetailIdAndDelYn(Long id, String delYn);
    Optional<Record> findByIdAndDelYn(Long id, String delYn);
//    Page<Record> findByDelYn(String delYn, Pageable pageable);
}