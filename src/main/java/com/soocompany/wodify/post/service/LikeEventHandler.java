//package com.soocompany.wodify.post.service;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.soocompany.wodify.common.config.RabbitMqConfig;
//import com.soocompany.wodify.member.domain.Member;
//import com.soocompany.wodify.member.repository.MemberRepository;
//import com.soocompany.wodify.post.domain.LikeEvent;
//import com.soocompany.wodify.post.domain.Likes;
//import com.soocompany.wodify.post.domain.Post;
//import com.soocompany.wodify.post.repository.LikeRepository;
//import com.soocompany.wodify.post.repository.PostRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityNotFoundException;
//import java.util.Optional;
//@Slf4j
//@Component
//@Transactional
//@RequiredArgsConstructor
//public class LikeEventHandler {
//
//    @Autowired
//    private final PostRepository postRepository;
//    private final MemberRepository memberRepository;
//    private final LikeRepository likeRepository;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @RabbitListener(queues = RabbitMqConfig.LIKE_EVENT_QUEUE)
//    public void likeEventListen(Message message) {
//        String messageBody = new String(message.getBody());
//        LikeEvent likeEvent;
//        try {
//            likeEvent = objectMapper.readValue(messageBody, LikeEvent.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        Post post = postRepository.findById(likeEvent.getPostId()).orElseThrow(() -> {
//            log.error("likeEventListen() : 해당 ID의 게시글을 찾을 수 없습니다.");
//            return new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다.");
//        });
//        if (post.getDelYn().equals("Y")) {
//            log.error("likeEventListen() : 삭제된 게시글 입니다.");
//            throw new IllegalArgumentException("삭제된 게시글 입니다.");
//        }
//        Member member = memberRepository.findByIdAndDelYn(likeEvent.getMemberId(), "N").orElseThrow(() -> {
//            log.error("commentDelete() : 해당 Email 의 member 가 없습니다.");
//            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
//        });
//        Optional<Likes> optionalLike = likeRepository.findByPostIdAndMemberId(likeEvent.getPostId(), likeEvent.getMemberId());
//        if (optionalLike.isPresent()) {
//            Likes likes = optionalLike.get();
//            if (likes.getDelYn().equals("N")) {
//                likes.updateDelYn();
//            } else {
//                likes.updateDelyN();
//            }
//        } else {
//            likeRepository.save(Likes.builder().member(member).post(post).build());
//        }
//        post.updateLikeCount(likeEvent.getLikeCount());
//        postRepository.save(post);
//    }
//}
//
//
