package com.soocompany.wodify.reservation_detail.service;

import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import com.soocompany.wodify.reservation_detail.controller.SseController;
import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetailDetResDto;
import com.soocompany.wodify.reservation_detail.repository.ReservationDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class ReservationScheduler {

    private final SseController sseController;
    private final ReservationRepository reservationRepository;
    private final ReservationDetailRepository reservationDetailRepository;

    public ReservationScheduler(SseController sseController, ReservationRepository reservationRepository, ReservationDetailRepository reservationDetailRepository) {
        this.sseController = sseController;
        this.reservationRepository = reservationRepository;
        this.reservationDetailRepository = reservationDetailRepository;
    }

    @Scheduled(cron="0 0/1 * * * *") // test
//    @Scheduled(cron="0 0/5 * * * *")  // test
    @Transactional
    public void alarmReservation(){
        LocalDate date = LocalDate.now();
        List<Reservation> reservationList = reservationRepository.findAllByDateAndDelYn(date, "N");
        for(Reservation reservation : reservationList){
            List<ReservationDetail> reservationDetails = reservationDetailRepository.findAllByReservationAndDelYn(reservation, "N");
            for(ReservationDetail reservationDetail : reservationDetails){
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                if(reservationDetail.getReservation().getTime().minusHours(1).format(dateTimeFormatter).equals(LocalTime.now().format(dateTimeFormatter))){
                    ReservationDetailDetResDto dto = reservationDetail.detFromEntity();
                    dto.setCheck("reservationDetail");
                    String memberId = String.valueOf(reservationDetail.getMember().getId());
                    sseController.publishReservationMessage(dto,memberId);
                }
            }
        }
    }
}
