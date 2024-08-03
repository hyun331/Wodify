package com.soocompany.wodify.reservation.repository;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.reservation.domain.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Page<Reservation> findByBoxAndAndDelYn(Box box, String delYn, Pageable pageable);

    Page<Reservation> findByBoxAndDateAndDelYn(Box box, LocalDate date, String delYn, Pageable pageable);
    Optional<Reservation> findByIdAndDelYn(Long id,String delYn);
}