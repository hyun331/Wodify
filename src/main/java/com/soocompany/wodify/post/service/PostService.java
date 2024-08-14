package com.soocompany.wodify.post.service;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.post.domain.Image;
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
        Post post = postRepository.save(dto.toEntity(member));
        try {
            if (dto.getFiles() != null && dto.getFiles().length > 0) {
                List<Image> images = imageService.uploadImages(post, dto.getFiles());
                post.getFiles().addAll(images);
                postRepository.save(post);
            }
        } catch (IOException e) {
            log.error("postCreate() : 이미지 저장 실패", e);
            throw new RuntimeException("이미지 저장 실패", e);
        }
        return post;
    }

    public List<PostListResDto> postListNotice() {
        List<Post> posts = postRepository.findAllByTypeAndDelYnOrderByCreatedTimeDesc(Type.NOTICE, "N");
        return posts.stream().map(PostListResDto::fromEntity).collect(Collectors.toList());
    }

    public Page<PostListResDto> postListPage(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByTypeAndDelYnOrderByCreatedTimeDesc(pageable, Type.POST, "N");
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
        if (postUpdateReqDto.getRemovedFileIds() != null) {
            for (Long fileId : postUpdateReqDto.getRemovedFileIds()) {
                imageService.imageDelete(fileId);
            }
        }
        post.updatePost(postUpdateReqDto);
        try {
            MultipartFile[] files = postUpdateReqDto.getNewFiles();
            if (files != null && files.length > 0) {
                List<Image> images = imageService.uploadImages(post, files);
                post.getFiles().addAll(images);
            }
        } catch (IOException e) {
            log.error("postCreate() : 이미지 저장 실패", e);
            throw new RuntimeException("이미지 저장 실패", e);
        }
        Post savedPost = postRepository.save(post);
        return PostDetResDto.fromEntity(savedPost);
    }
}
