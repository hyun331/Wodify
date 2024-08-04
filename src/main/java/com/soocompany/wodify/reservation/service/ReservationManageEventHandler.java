package com.soocompany.wodify.reservation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soocompany.wodify.common.config.RabbitMqConfig;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.dto.ReservationManageEvent;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Component
public class ReservationManageEventHandler {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private final ReservationRepository reservationRepository;

    public ReservationManageEventHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void publish(ReservationManageEvent event) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.RESERVATION_MANAGE_QUEUE, event);
    }
    @Transactional
    @RabbitListener(queues = RabbitMqConfig.RESERVATION_MANAGE_QUEUE)
    public void listen(Message message) {
        String messageBody = new String(message.getBody());
        System.out.println(messageBody);
//        json 메시지를 ObjectMapper로 직접 parsing
        ObjectMapper objectMapper = new ObjectMapper();
        ReservationManageEvent dto = null;
        try {
            dto = objectMapper.readValue(messageBody, ReservationManageEvent.class);

//          인원 업데이트
            Reservation reservation = reservationRepository.findByIdAndDelYn(dto.getReservationId(), "N").orElseThrow(() -> new EntityNotFoundException("해당 예약을 찾을 수 없습니다."));
            reservation.decreaseAvailablePeople();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
