package com.soocompany.wodify.post.service;
import com.soocompany.wodify.common.config.RabbitMqConfig;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.post.domain.LikeEvent;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.dto.PostLikeReqDto;
import com.soocompany.wodify.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
public class LikeService {

    private final RedisTemplate<String, Object> redisLikeTemplate;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final RabbitTemplate rabbitTemplate;

    public LikeService(@Qualifier("redisLikeTemplate") RedisTemplate<String, Object> redisLikeTemplate, PostRepository postRepository, MemberRepository memberRepository, RabbitTemplate rabbitTemplate) {
        this.redisLikeTemplate = redisLikeTemplate;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Long postLike(PostLikeReqDto postLikeReqDto) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postLike() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        Post post = postRepository.findById(postLikeReqDto.getId()).orElseThrow(() -> {
            log.error("postLike() : 해당 id의 게시글을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다.");
        });
        if (post.getDelYn().equals("Y")) {
            log.error("postLike() : 삭제된 게시글 입니다.");
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        }
        String likeStatusKey = "post::" + post.getId() + "member::" + member.getId() + "::likeStatus";
        String likeCountKey = "post::" + post.getId() + "::likeCount";

        Boolean liked = redisLikeTemplate.hasKey(likeStatusKey);
        Long likeCount;

        if (Boolean.TRUE.equals(liked)) {
            redisLikeTemplate.delete(likeStatusKey);
            likeCount = redisLikeTemplate.opsForValue().decrement(likeCountKey);
        } else {
            if (Boolean.FALSE.equals(redisLikeTemplate.hasKey(likeCountKey))) {
                redisLikeTemplate.opsForValue().set(likeCountKey, 0);
            }
            redisLikeTemplate.opsForValue().set(likeStatusKey, "Y");
            likeCount = redisLikeTemplate.opsForValue().increment(likeCountKey);
        }

        rabbitTemplate.convertAndSend(RabbitMqConfig.LIKE_EVENT_QUEUE, "post.like", new LikeEvent(post.getId(), member.getId(), likeCount));
        return likeCount;
    }
}
