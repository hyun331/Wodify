package com.soocompany.wodify.reservation_detail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soocompany.wodify.reservation_detail.dto.ReservationDetailDetResDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@RestController
public class SseController implements MessageListener {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    private Set<String> subscribeList = ConcurrentHashMap.newKeySet();

    @Qualifier("5")
    private final RedisTemplate<String, Object> sseRedisTemplate;
    @Qualifier("5")
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    @Qualifier("6")
    private final RedisTemplate<String, Object> reservationRedisTemplate;


    public SseController(@Qualifier("5") RedisTemplate<String, Object> sseRedisTemplate, @Qualifier("5") RedisMessageListenerContainer redisMessageListenerContainer,
                         @Qualifier("6")RedisTemplate<String, Object> reservationRedisTemplate) {
        this.sseRedisTemplate = sseRedisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.reservationRedisTemplate = reservationRedisTemplate;
    }


    public void subscribeChannel(String email){
        if(!subscribeList.contains(email)){
            MessageListenerAdapter listenerAdapter = createListenerAdapter(this);
            redisMessageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic(email));
            subscribeList.add(email);
        }
    }

    public MessageListenerAdapter createListenerAdapter(SseController sseController){
        return new MessageListenerAdapter(sseController, "onMessage");
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(14400 * 60 * 1000L);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String memberId = authentication.getName();

        emitters.put(memberId, emitter);
        emitter.onCompletion(() -> emitters.remove((memberId)));
        emitter.onTimeout(() -> emitters.remove(memberId));

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected!"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        subscribeChannel(memberId);
        return emitter;
    }

    public void publishMessage(ReservationDetailDetResDto dto, String memberId){
        SseEmitter emitter = emitters.get(memberId);
        sseRedisTemplate.convertAndSend(memberId, dto);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ObjectMapper objectMapper = new ObjectMapper();
        String email = new String(pattern, StandardCharsets.UTF_8);
        try {
            System.out.println("listening");
            ReservationDetailDetResDto dto = objectMapper.readValue(message.getBody(), ReservationDetailDetResDto.class);
            SseEmitter emitter = emitters.get(email);
            if(emitter!=null){
                if(dto.getCheck().equals("reservation")){
                    emitter.send(SseEmitter.event().name("reservation").data(dto));
                }
                if(dto.getCheck().equals("reservationDetail")){
                    emitter.send(SseEmitter.event().name("reservationDetail").data(dto));
                }
            }
            System.out.println(dto);
        } catch (IOException e) {
            log.error("IOException while sending SSE: {}", e.getMessage());
            emitters.remove(email); // 해당 클라이언트의 Emitter 제거
        } catch (IllegalStateException e) {
            log.error("IllegalStateException while sending SSE: {}", e.getMessage());
            emitters.remove(email); // 해당 클라이언트의 Emitter 제거
        }
    }

    public void publishReservationMessage(ReservationDetailDetResDto dto, String memberId) {
        SseEmitter emitter = emitters.get(memberId);
        reservationRedisTemplate.convertAndSend(memberId, dto);
    }

}