package com.soocompany.wodify.post.service;
import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.domain.Type;
import com.soocompany.wodify.post.dto.*;
import com.soocompany.wodify.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    public Post postCreate(PostSaveReqDto dto) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postCreate() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        Box box = member.getBox();
        if (box == null) { throw new IllegalArgumentException("box 에 등록되지 않은 사용자입니다.");}
        return postRepository.save(dto.toEntity(member));
    }

    public List<PostListResDto> postListNotice() {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postListNotice() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        List<Post> posts = postRepository.findAllByTypeAndBoxAndDelYnOrderByCreatedTimeDesc(Type.NOTICE, member.getBox(), "N");
        return posts.stream().map(PostListResDto::fromEntity).collect(Collectors.toList());
    }

    public Page<PostListResDto> postListPage(Pageable pageable) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postCreate() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        Page<Post> posts = postRepository.findAllByTypeAndBoxAndDelYnOrderByCreatedTimeDesc(pageable, Type.POST, member.getBox(), "N");
        return posts.map(PostListResDto::fromEntity);
    }

    public PostDetResDto postDetail(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> {
            log.error("postDetail() : 해당 id의 게시글을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다.");
        });
        if (post.getDelYn().equals("Y")) {
            log.error("postDetail() : 삭제된 게시글 입니다.");
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        }
        return PostDetResDto.fromEntity(post);
    }

    public void postDelete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("postDelete() : 해당 id의 게시글을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다.");
        });
        if (post.getDelYn().equals("Y")) {
            log.error("postDelete() : 삭제된 게시글 입니다.");
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        }
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postDelete() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        if (!post.getMember().getId().equals(Long.parseLong(id))) {
            log.error("postDelete() : 본인의 게시글이 아닙니다.");
            throw new IllegalArgumentException("본인의 게시글이 아닙니다.");
        }
        post.deletePost();
    }

    public PostDetResDto postUpdate(Long postId, PostUpdateReqDto postUpdateReqDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("postUpdate() : 해당 id의 게시글을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다.");
        });
        if (post.getDelYn().equals("Y")) {
            log.error("postUpdate() : 삭제된 게시글 입니다.");
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        }
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postUpdate() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        if (!post.getMember().getId().equals(member.getId())) {
            log.error("postUpdate() : 본인의 게시글이 아닙니다.");
            throw new IllegalArgumentException("본인의 게시글이 아닙니다.");
        }
        post.updatePost(postUpdateReqDto);
        Post savedPost = postRepository.save(post);
        return PostDetResDto.fromEntity(savedPost);
    }
}
