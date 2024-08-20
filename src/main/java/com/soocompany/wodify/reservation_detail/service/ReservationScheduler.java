//package com.soocompany.wodify.reservation_detail.service;
//
//import com.soocompany.wodify.member.domain.Member;
//import com.soocompany.wodify.member.domain.Role;
//import com.soocompany.wodify.member.repository.MemberRepository;
//import com.soocompany.wodify.reservation.domain.Reservation;
//import com.soocompany.wodify.reservation.repository.ReservationRepository;
//import com.soocompany.wodify.reservation_detail.controller.SseController;
//import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
//import com.soocompany.wodify.reservation_detail.dto.ReservationDetailDetResDto;
//import com.soocompany.wodify.reservation_detail.repository.ReservationDetailRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityNotFoundException;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//@Component
//@Slf4j
//public class ReservationScheduler {
//
//    // 매 분마다..? 한시간 후의 예약을 찾아서
//    // 알림을 날린다.
//    // 스케줄러에 sseController를 넣는다.
//
////    rList : 예약에서 오늘 날짜의 예약들을 고른다.
////    rdList : 예약 내역에서 오늘 날짜의 예약들을 통해 예약 내역들을 고른다.
////    위의 내역들의 시간에 따라 알림을 날린다.
//
//    private final SseController sseController;
//    private final ReservationRepository reservationRepository;
//    private final ReservationDetailRepository reservationDetailRepository;
//    private final MemberRepository memberRepository;
//
//    public ReservationScheduler(SseController sseController, ReservationRepository reservationRepository, ReservationDetailRepository reservationDetailRepository, MemberRepository memberRepository) {
//        this.sseController = sseController;
//        this.reservationRepository = reservationRepository;
//        this.reservationDetailRepository = reservationDetailRepository;
//        this.memberRepository = memberRepository;
//    }
//
//    @Scheduled(cron="0 0/1 * * * *")
//    @Transactional
//    public void alarmReservation(){
//        System.out.println("안되는거니?!");
//        LocalDate date = LocalDate.now();
//        List<Reservation> reservationList = reservationRepository.findAllByDateAndDelYn(date, "N");
//        for(Reservation reservation : reservationList){
//            List<ReservationDetail> reservationDetails = reservationDetailRepository.findAllByReservationAndDelYn(reservation, "N");
//            for(ReservationDetail reservationDetail : reservationDetails){
//                if(reservationDetail.getReservation().getTime().minusHours(1).equals(LocalTime.now())){
//                    System.out.println(reservationDetail.getReservation().getTime().minusHours(1));
//                    System.out.println(LocalTime.now());
//                    ReservationDetailDetResDto dto = reservationDetail.detFromEntity();
//                    String memberId = String.valueOf(reservationDetail.getMember().getId());
//                    sseController.publishReservationMessage(dto,memberId);
//                }
//            }
//        }
//
//
////        ReservationDetailDetResDto dto = ReservationDetailDetResDto.builder().build();
////        String memberId = "7";
////        sseController.publishReservationMessage(dto,memberId);
//    }
//
//}
