package com.soocompany.wodify.post.service;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.dto.PostDetResDto;
import com.soocompany.wodify.post.dto.PostListResDto;
import com.soocompany.wodify.post.dto.PostSaveReqDto;
import com.soocompany.wodify.post.dto.PostUpdateReqDto;
import com.soocompany.wodify.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Post postCreate(String email, PostSaveReqDto dto) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("postCreate() : Email 에 해당하는 member 가 없습니다.");
        Member member = memberRepository.findByEmailAndDelYn(email, "N")
            .orElseThrow(() -> new EntityNotFoundException("Email 에 해당하는 member 가 없습니다."));
        return postRepository.save(dto.toEntity(member));
    }

    public Page<PostListResDto> postListPage(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByDelYn(pageable, "N");
        return posts.map(post -> new PostListResDto().listFromEntity(post));
    }

    public PostDetResDto postDetail(Long id) {
        log.info("postDetail() : 해당 id의 게시글을 찾을 수 없습니다.");
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다."));
        if (post.getDelYn().equals("Y"))
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        return PostDetResDto.fromEntity(post);
    }

    public void postDelete(Long id, String email) {
        log.info("postDelete() : 해당 id의 게시글을 찾을 수 없습니다.");
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다."));
        log.info("postDelete() : 삭제된 게시글 입니다.");
        if (post.getDelYn().equals("Y"))
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("postDelete() : 본인의 게시글이 아닙니다.");
        if (!post.getMember().getEmail().equals(email))
            throw new IllegalArgumentException("본인의 게시글이 아닙니다.");
        post.updateDelYn();
    }

    public PostDetResDto postUpdate(Long id, String email, PostUpdateReqDto postUpdateReqDto) {
        log.info("postUpdate() : 해당 id의 게시글을 찾을 수 없습니다.");
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다."));
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("postDelete() : 삭제된 게시글 입니다.");
        if (post.getDelYn().equals("Y"))
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        log.info("postUpdate() : 본인의 게시글이 아닙니다.");
        if (!post.getMember().getEmail().equals(email))
            throw new IllegalArgumentException("본인의 게시글이 아닙니다.");
        post.updatePost(postUpdateReqDto);
        Post savedPost = postRepository.save(post);
        return PostDetResDto.fromEntity(savedPost);
    }
}
