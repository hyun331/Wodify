package com.soocompany.wodify.record.repository;

import com.soocompany.wodify.record.domain.RecordDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordDetailRepository extends JpaRepository<RecordDetail, Long> {
}
