package com.soocompany.wodify.reservation.repository;
import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.reservation.domain.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Page<Reservation> findByBoxAndDelYn(Box box, String delYn, Pageable pageable);
    List<Reservation> findByBoxAndDateAndDelYn(Box box, LocalDate date, String delYn);
    List<Reservation> findAllByBoxAndDateAndDelYn(Box box, LocalDate date, String delYn);
    Optional<Reservation> findByIdAndDelYn(Long id,String delYn);
    Page<Reservation> findByBoxAndDateBetweenAndDelYn(Box box, LocalDate startDate, LocalDate endDate,String delYn,Pageable pageable);

    boolean existsByBoxAndDateAndTimeAndDelYn(Box box, LocalDate date, LocalTime time, String delYn);

    List<Reservation> findAllByDateAndDelYn(LocalDate date, String delYn);
}