package com.soocompany.wodify.reservation.service;

import com.soocompany.wodify.common.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class ReservationManageEventHandler {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(ReservationManageEventHandler event) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.RESERVATION_MANAGE_QUEUE, event);
    }
    @Transactional
    @RabbitListener(queues = RabbitMqConfig.RESERVATION_MANAGE_QUEUE)
    public void listen() {

    }
}
