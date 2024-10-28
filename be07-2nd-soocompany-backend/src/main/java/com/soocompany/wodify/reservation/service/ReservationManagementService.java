package com.soocompany.wodify.reservation.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReservationManagementService {
    private final RedisTemplate<String, Object> redisTemplate;

    public ReservationManagementService(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Long increaseAvailable(Long reservationId, int people) {
        return redisTemplate.opsForValue().increment(String.valueOf(reservationId), people);
    }
    public Long decreaseAvailable(Long reservationId,int people) {
        Object remains = redisTemplate.opsForValue().get(String.valueOf(reservationId));
        int longRemains = Integer.parseInt(remains.toString());
        if (longRemains == 0) {
            return -1L;
        } else {
            return redisTemplate.opsForValue().decrement(String.valueOf(reservationId), people);
        }
    }
}
