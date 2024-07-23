package com.soocompany.wodify.wodDetail.repository;

import com.soocompany.wodify.wodDetail.domain.WodDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WodDetailRepository extends JpaRepository<WodDetail,Long> {
}
