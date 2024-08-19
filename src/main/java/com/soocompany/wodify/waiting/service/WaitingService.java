package com.soocompany.wodify.waiting.service;

import com.soocompany.wodify.waiting.dto.WaitingCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@Slf4j
public class WaitingService {


    private final ZSetOperations<String, String> zSetOperations;
    @Autowired
    public WaitingService(@Qualifier("ZsetTemplate") RedisTemplate<String, String> redisTemplate) {
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    public void addToQueue(WaitingCreateDto dto) {
        log.info(String.valueOf(dto.getReservationId()));
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        String key = getQueueKey(String.valueOf(dto.getReservationId()));
        long score = Instant.now().toEpochMilli();  // 시간을 기준으로 우선순위두기
        zSetOperations.add(key, memberId, score);
    }

    public String getNextMember(String reservationId) {
        String key = getQueueKey(reservationId);
        Set<String> members = zSetOperations.range(key, 0, 0); //대기 첫번째 멤버 가져오기
        if (members != null && !members.isEmpty()) {
            return members.iterator().next();
        }
        return null;
    }

    public void removeFromQueue(String reservationId, String memberId) {
        String key = getQueueKey(reservationId);
        zSetOperations.remove(key, memberId);
    }

    private String getQueueKey(String reservationId) {
        return "waiting_list:" + reservationId ;
    }

    public Set<String> getWaitingList(String reservationId) {
        String key = getQueueKey(reservationId);
        return zSetOperations.range(key, 0, -1);
    }
}
