package com.soocompany.wodify.reservation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.soocompany.wodify.common.config.RabbitMqConfig;
import com.soocompany.wodify.reservation.domain.Reservation;
import com.soocompany.wodify.reservation.dto.ReservationManageEvent;
import com.soocompany.wodify.reservation.repository.ReservationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;

@Component
@Slf4j
public class ReservationManageEventHandler {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private final ReservationRepository reservationRepository;

    public ReservationManageEventHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void publish(ReservationManageEvent event) {
        log.info("mq 활동중 영차영차 : publish : reservation = " + event.getReservationId());
        try {
            rabbitTemplate.convertAndSend(RabbitMqConfig.RESERVATION_MANAGE_QUEUE, event);
            log.info("mq 메시지 전송 성공");
        } catch (AmqpException e) {
            log.error("mq 메시지 전송 실패: ", e);
            // 예외 처리 로직 추가
        }catch (Exception e) {
            log.error("mq 메시지 전송 실패: ", e);
        }
    }
    @Transactional
    @RabbitListener(queues = RabbitMqConfig.RESERVATION_MANAGE_QUEUE)
    public void listen(Message message, Channel channel) {
        String messageBody = new String(message.getBody());
//        json 메시지를 ObjectMapper로 직접 parsing
        ObjectMapper objectMapper = new ObjectMapper();
        ReservationManageEvent dto = null;
        log.info("mq 활동중 영차영차 : listen을 하긴 함");
        try {
            dto = objectMapper.readValue(messageBody, ReservationManageEvent.class);
            log.info("mq 활동중 영차영차 : listen : " + dto.toString());

            // 인원 업데이트
            Reservation reservation = reservationRepository.findByIdAndDelYn(dto.getReservationId(), "N")
                    .orElseThrow(() -> new EntityNotFoundException("해당 예약을 찾을 수 없습니다."));
            reservation.decreaseAvailablePeople();

            // 메시지 처리 성공 시 ACK 전송
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 에러: ", e);
        } catch (Exception e) {
            log.error("메시지 처리 실패: ", e);
        }

    }
}
