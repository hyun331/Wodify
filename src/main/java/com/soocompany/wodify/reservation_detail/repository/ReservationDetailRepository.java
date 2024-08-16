package com.soocompany.wodify.reservation_detail.repository;

import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationDetailRepository extends JpaRepository<ReservationDetail,Long> {
    Optional<ReservationDetail> findByIdAndDelYn(Long id, String delYn);
    @Query("SELECT rd FROM ReservationDetail rd " +
            "JOIN rd.reservation r " +
            "JOIN rd.member m " +
            "WHERE rd.delYn = 'N' " +
            "AND m = :member " +
            "ORDER BY r.date DESC")
    Page<ReservationDetail> findByMemberAndDelYn(@Param("member") Member member, Pageable pageable);
    List<ReservationDetail> findByReservationAndDelYn(Reservation reservation, String delYn);
    @Query("SELECT rd FROM ReservationDetail rd " +
            "JOIN rd.reservation r " +
            "JOIN rd.member m " +
            "WHERE r.date BETWEEN :startDate AND :endDate " +
            "AND rd.delYn = 'N' " +
            "AND m = :member " +
            "ORDER BY r.date DESC")
    Page<ReservationDetail> findAllByMemberAndDateRange(
            @Param("member") Member member,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

}
