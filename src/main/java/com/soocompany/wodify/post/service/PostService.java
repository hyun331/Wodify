package com.soocompany.wodify.post.service;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.post.domain.Comment;
import com.soocompany.wodify.post.domain.Image;
import com.soocompany.wodify.post.domain.Likes;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.dto.*;
import com.soocompany.wodify.post.repository.CommentRepository;
import com.soocompany.wodify.post.repository.ImageRepository;
import com.soocompany.wodify.post.repository.LikeRepository;
import com.soocompany.wodify.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final LikeRepository likeRepository;
    private final S3Client s3Client;

    public Post postCreate(PostSaveReqDto dto) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postCreate() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        Post post = postRepository.save(dto.toEntity(member));
        try {
            for (MultipartFile file : dto.getFiles()) {
                byte[] bytes = file.getBytes();
                String fileName = post.getId() + "_" + file.getOriginalFilename();

                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

                PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
                String s3Path = s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(fileName)).toExternalForm();
                Image image = Image.builder()
                        .member(member)
                        .post(post)
                        .fileName(fileName)
                        .s3Path(s3Path)
                        .build();

                imageRepository.save(image);
                post.getFiles().add(image);
            }
            postRepository.save(post);
        } catch (IOException e) {
            log.error("postCreate() : 이미지 저장 실패");
            throw new RuntimeException("이미지 저장 실패");
        }
        return post;
    }

    public Page<PostListResDto> postListPage(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByDelYn(pageable, "N");
        return posts.map(post -> new PostListResDto().listFromEntity(post));
    }

    public PostDetResDto postDetail(PostDetReqDto postDetReqDto) {
        Post post = postRepository.findById(postDetReqDto.getId()).orElseThrow(() -> {
            log.error("postDetail() : 해당 id의 게시글을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다.");
        });
        if (post.getDelYn().equals("Y")) {
            log.error("postDetail() : 삭제된 게시글 입니다.");
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        }
        return PostDetResDto.fromEntity(post);
    }

    public void postDelete(PostDelReqDto postDelReqDto) {
        Post post = postRepository.findById(postDelReqDto.getId()).orElseThrow(() -> {
            log.error("postDelete() : 해당 id의 게시글을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다.");
        });
        if (post.getDelYn().equals("Y")) {
            log.error("postDelete() : 삭제된 게시글 입니다.");
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmailAndDelYn(email, "N").orElseThrow(() -> {
            log.error("postDelete() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        if (!post.getMember().getEmail().equals(email)) {
            log.error("postDelete() : 본인의 게시글이 아닙니다.");
            throw new IllegalArgumentException("본인의 게시글이 아닙니다.");
        }
        post.deletePost();
    }

    public void imageDelete(ImageDelReqDto imageDelReqDto) {
        Image image = imageRepository.findById(imageDelReqDto.getId()).orElseThrow(() -> {
            log.error("imageDelete() : 해당 id의 image 를 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 image 를 찾을 수 없습니다.");
        });
        if (image.getDelYn().equals("Y")) {
            log.error("imageDelete() : 삭제된 image 입니다.");
            throw new IllegalArgumentException("삭제된 image 입니다.");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmailAndDelYn(email, "N").orElseThrow(() -> {
            log.error("imageDelete() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        if (!image.getMember().getEmail().equals(email)) {
            log.error("imageDelete() : 본인의 image 가 아닙니다.");
            throw new IllegalArgumentException("본인의 게시글이 아닙니다.");
        }
        image.updateDelYn();
    }

    public PostDetResDto postUpdate(PostUpdateReqDto postUpdateReqDto) {
        Post post = postRepository.findById(postUpdateReqDto.getId()).orElseThrow(() -> {
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
        if (!post.getMember().getId().equals(Long.parseLong(id))) {
            log.error("postUpdate() : 본인의 게시글이 아닙니다.");
            throw new IllegalArgumentException("본인의 게시글이 아닙니다.");
        }
        post.updatePost(postUpdateReqDto);
        Post savedPost = postRepository.save(post);
        return PostDetResDto.fromEntity(savedPost);
    }

    public Post commentCreate(CommentSaveReqDto commentSaveReqDto) {
        Post post = postRepository.findById(commentSaveReqDto.getPostId()).orElseThrow(() -> {
            log.error("commentCreate() : 해당 id의 게시글을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다.");
        });
        if (post.getDelYn().equals("Y")) {
            log.error("commentCreate() : 삭제된 게시글 입니다.");
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        }
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("commentCreate() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        if (commentSaveReqDto.getParentId() != null) {
            Comment parentComment = commentRepository.findById(Long.parseLong(commentSaveReqDto.getParentId())).orElseThrow(() -> {
                log.error("commentCreate() : id 에 해당하는 comment 가 없습니다.");
                return new EntityNotFoundException("id 에 해당하는 comment 가 없습니다.");
            });
            Comment savedComment = commentRepository.save(commentSaveReqDto.toEntity(member, post, parentComment));
            parentComment.getReplies().add(savedComment);
            commentRepository.save(parentComment);
        } else {
            Comment savedComment = commentRepository.save(commentSaveReqDto.toEntity(member, post, null));
            post.getComments().add(savedComment);
        }
        return post;
    }

    public void commentDelete(CommentDelReqDto commentDelReqDto) {
        Comment comment = commentRepository.findById(commentDelReqDto.getId()).orElseThrow(() -> {
            log.error("commentDelete() : 해당 id의 댓글을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 댓글을 찾을 수 없습니다.");
        });
        if (comment.getDelYn().equals("Y")) {
            log.error("commentDelete() : 삭제된 댓글 입니다.");
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        }
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("commentDelete() : 해당 Email 의 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        if (!comment.getMember().getId().equals(Long.parseLong(id))) {
            log.error("commentDelete() : 본인의 댓글이 아닙니다.");
            throw new IllegalArgumentException("본인의 댓글이 아닙니다.");
        }
        comment.updateDelYn();
    }

    public PostDetResDto commentUpdate(CommentUpdateReqDto commentUpdateReqDto) {
        Comment comment = commentRepository.findById(commentUpdateReqDto.getId()).orElseThrow(() -> {
            log.error("commentUpdate() : 해당 id의 댓글을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 댓글을 찾을 수 없습니다.");
        });
        if (comment.getDelYn().equals("Y")) {
            log.error("commentUpdate() : 삭제된 댓글 입니다.");
            throw new IllegalArgumentException("삭제된 댓글 입니다.");
        }
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("commentUpdate() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        if (!comment.getMember().getId().equals(Long.parseLong(id))) {
            log.error("commentUpdate() : 본인의 댓글이 아닙니다.");
            throw new IllegalArgumentException("본인의 댓글이 아닙니다.");
        }
        comment.updateComment(commentUpdateReqDto);
        return PostDetResDto.fromEntity(comment.getPost());
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
        Optional<Likes> optionalLike = likeRepository.findByPostIdAndMemberId(postLikeReqDto.getId(), member.getId());
        if (optionalLike.isPresent()) {
                Likes likes = optionalLike.get();
            if (likes.getDelYn().equals("N")) {
                likes.updateDelYn();
                return post.updateLikeCount(-1L);
            } else {
                likes.updateDelyN();
                return post.updateLikeCount(+1L);
            }
        } else {
            likeRepository.save(Likes.builder().member(member).post(post).build());
            return post.updateLikeCount(+1L);
        }
    }
}
