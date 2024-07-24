package com.soocompany.wodify.reservation_detail.repository;

import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationDetailRepository extends JpaRepository<ReservationDetail,Long> {
    Optional<ReservationDetail> findByIdAndDelYn(Long id, String delYn);
}
