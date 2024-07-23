package com.soocompany.wodify.reservation.repository;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    List<Reservation> findByBoxAndAndDelYn(Box box, String delYn);


    Optional<Reservation> findByIdAndDelYn(Long id,String delYn);
}
