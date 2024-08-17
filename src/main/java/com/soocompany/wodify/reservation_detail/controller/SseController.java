package com.soocompany.wodify.reservation_detail.controller;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.soocompany.wodify.reservation_detail.domain.ReservationDetail;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.connection.MessageListener;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.PatternTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//@RestController
//public class SseController implements MessageListener {
//
//    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
//
//    private Set<String> subscribeList = ConcurrentHashMap.newKeySet();
//
//    @Qualifier("5")
//    private final RedisTemplate<String, Object> sseRedisTemplate;
//
//    private final RedisMessageListenerContainer redisMessageListenerContainer;
//
//    public SseController(@Qualifier("5") RedisTemplate<String, Object> sseRedisTemplate, RedisMessageListenerContainer redisMessageListenerContainer) {
//        this.sseRedisTemplate = sseRedisTemplate;
//        this.redisMessageListenerContainer = redisMessageListenerContainer;
//    }
//
//
//    public void subscribeChannel(String email){
//        if(!subscribeList.contains(email)){
//            MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(this);
//            redisMessageListenerContainer.addMessageListener(listenerAdapter, new PatternTopic(email));
//            subscribeList.add(email);
//        }
//    }
//
//    public MessageListenerAdapter createListenerAdapter(SseController sseController){
//        return new MessageListenerAdapter(sseController, "onMessage");
//    }
//
//    @GetMapping("/subscribe")
//    public SseEmitter subscribe() {
//        SseEmitter emitter = new SseEmitter(14400 * 60 * 1000L);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//        emitters.put(email, emitter);
//        emitter.onCompletion(() -> emitters.remove((email)));
//        emitter.onTimeout(() -> emitters.remove(email));
//
//        try {
//            emitter.send(SseEmitter.event().name("connect").data("connected!"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        subscribeChannel(email);
//        return emitter;
//    }
//
//    public void publishMessage(ReservationDetail dto, String email){
//        SseEmitter emitter = emitters.get(email);
//        sseRedisTemplate.convertAndSend(email, dto);
////        }
//    }
//
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            System.out.println("listening");
//            ReservationDetail dto = objectMapper.readValue(message.getBody(), ReservationDetail.class);
//            String email = new String(pattern, StandardCharsets.UTF_8);
//            SseEmitter emitter = emitters.get(email);
//            if(emitter!=null){
//                emitter.send(SseEmitter.event().name("reservation").data(dto));
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}