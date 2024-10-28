package com.soocompany.wodify.post.service;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.post.domain.Comment;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.dto.*;
import com.soocompany.wodify.post.repository.CommentRepository;
import com.soocompany.wodify.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public CommentResDto commentCreate(Long postId, CommentSaveReqDto commentSaveReqDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
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
            return CommentResDto.fromEntity(savedComment);
        } else {
            Comment savedComment = commentRepository.save(commentSaveReqDto.toEntity(member, post, null));
            post.getComments().add(savedComment);
            return CommentResDto.fromEntity(savedComment);
        }
    }

    public void commentDelete(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
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

    public PostListResDto commentUpdate(Long commentId, CommentUpdateReqDto commentUpdateReqDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
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
        return PostListResDto.fromEntity(comment.getPost());
    }
}
