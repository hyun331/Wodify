package com.soocompany.wodify.record.repository;

import com.soocompany.wodify.record.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordReository extends JpaRepository<Record, Long> {
}
