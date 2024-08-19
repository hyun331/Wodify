package com.soocompany.wodify.reservation_detail.service;

import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import com.soocompany.wodify.reservation_detail.repository.ReservationDetailRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class ReservationScheduler {

    // 매 분마다..? 한시간 후의 예약을 찾아서
    // 알림을 날린다.
    // 스케줄러에 sseController를 넣는다.

//    rList : 예약에서 오늘 날짜의 예약들을 고른다.
//    rdList : 예약 내역에서 오늘 날짜의 예약들을 통해 예약 내역들을 고른다.
//    위의 내역들의 시간에 따라 알림을 날린다.


    private final ReservationRepository reservationRepository;
    private final ReservationDetailRepository reservationDetailRepository;

    public ReservationScheduler(ReservationRepository reservationRepository, ReservationDetailRepository reservationDetailRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationDetailRepository = reservationDetailRepository;
    }

    @Scheduled(cron="0 0/1 * * * *")
    @Transactional
    public void alarmReservation(){
        LocalDate date = LocalDate.now();
        List<Reservation> reservationList = reservationRepository.findAllByDateAndDelYn(date, "N");
        for(Reservation reservation : reservationList){
            List<ReservationDetail> reservationDetails = reservationDetailRepository.findByReservationAndDelYn(reservation, "N");
            for(ReservationDetail reservationDetail : reservationDetails){
                LocalTime time = LocalTime.now();
                if(reservationDetail.getReservation().getTime().minusHours(1) == LocalTime.now()){
//                  여기서 알림 날려요.
                }
            }
        }
    }

}
