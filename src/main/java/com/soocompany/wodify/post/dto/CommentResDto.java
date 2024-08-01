package com.soocompany.wodify.post.dto;

import com.soocompany.wodify.post.domain.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResDto{
    private Long id;
    private String email;
    private String comment;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    static public CommentResDto fromEntity(Comment comment) {
        return CommentResDto.builder()
                .id(comment.getId())
                .email(comment.getMember().getEmail())
                .comment(comment.getComment())
                .createdTime(comment.getCreatedTime())
                .updatedTime(comment.getUpdatedTime())
                .build();
    }
}
