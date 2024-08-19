package com.soocompany.wodify.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    public String host;
    @Value("${spring.redis.port}")
    public int port;

    @Bean
    public RedisConnectionFactory redisReservationFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(1);
        return new LettuceConnectionFactory(configuration);
    }

    @Primary
    @Bean(name = "redisTemplate")
    public RedisTemplate<String,Object> redisStockTemplate(RedisConnectionFactory redisReservationFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisReservationFactory);
        return redisTemplate;
    }


    @Bean
    public RedisConnectionFactory redisPostFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(2);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean(name = "redisLikeTemplate")
    public RedisTemplate<String,Object> redisLikeTemplate(RedisConnectionFactory redisPostFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisPostFactory);
        return redisTemplate;
    }


    //refresh token
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(3);
        return new LettuceConnectionFactory(configuration);
    }
    @Bean(name = "refreshToken")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }



    @Bean(name="redisZsetFactory")
    public RedisConnectionFactory redisZsetFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(4);
        return new LettuceConnectionFactory(configuration);
    }
    @Bean(name = "zsetTemplate")
    public RedisTemplate<String, String> redisZsetTemplate(@Qualifier("redisZsetFactory")RedisConnectionFactory redisZsetFactory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisZsetFactory);
        return redisTemplate;
    }

    @Bean(name = "zsetRedisMessageListenerContainer")
    public RedisMessageListenerContainer zsetMessageListenerContainer(@Qualifier("redisZsetFactory")RedisConnectionFactory redisZsetFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisZsetFactory);
        return container;
    }




    @Bean(name="reservationFactory")
    public RedisConnectionFactory sseFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(4);
        return new LettuceConnectionFactory(configuration);

    }

    @Bean(name = "reservationRedisTemplate")
    public RedisTemplate<String, Object> sseRedisTemplate(@Qualifier("reservationFactory") RedisConnectionFactory sseFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        serializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setConnectionFactory(sseFactory);
        return redisTemplate;
    }

    @Bean(name = "reservationRedisMessageListenerContainer")
    public RedisMessageListenerContainer redisReservationMessageListenerContainer(@Qualifier("reservationFactory")RedisConnectionFactory sseFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(sseFactory);
        return container;
    }





    @Bean(name="reservationDetailFactory")
    public RedisConnectionFactory reservationFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(6);
        return new LettuceConnectionFactory(configuration);

    }

    @Bean(name = "reservationDetailRedisTemplate")
    public RedisTemplate<String, Object> reservationRedisTemplate(@Qualifier("reservationDetailFactory") RedisConnectionFactory reservationFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        serializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setConnectionFactory(reservationFactory);
        return redisTemplate;
    }

    @Bean(name = "reservationDetailRedisMessageListenerContainer")
    public RedisMessageListenerContainer redisReservationDetailMessageListenerContainer(@Qualifier("reservationDetailFactory") RedisConnectionFactory reservationFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(reservationFactory);
        return container;
    }



//    @Bean
//    public RedisMessageListenerContainer redisMessageListenerContainer(@Qualifier("redisZsetFactory")RedisConnectionFactory redisZsetFactory, @Qualifier("reservationFactory") RedisConnectionFactory reservationFactory){
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(redisZsetFactory);
//        container.setConnectionFactory(reservationFactory);
//        return container;
//    }


}
