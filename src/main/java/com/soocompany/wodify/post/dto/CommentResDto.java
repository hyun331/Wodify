package com.soocompany.wodify.post.dto;

import com.soocompany.wodify.post.domain.Comment;
import com.soocompany.wodify.post.domain.DateTimeFormatterUtil;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResDto{
    private Long id;
    private Long memberId;
    private String name;
    private String comment;
    private List<CommentResDto> replies;
    private String createdTime;
    private LocalDateTime updatedTime;
    private String delYn;

    static public CommentResDto fromEntity(Comment comment) {
        return CommentResDto.builder()
                .id(comment.getId())
                .memberId(comment.getMember().getId())
                .name(comment.getMember().getName())
                .comment(comment.getComment())
                .replies(comment.getReplies().stream()
                        .filter(comment1 -> comment1.getDelYn().equals("N"))
                        .map(CommentResDto::fromEntity)
                        .collect(Collectors.toList())
                )
                .createdTime(DateTimeFormatterUtil.dateTime(comment.getCreatedTime()))
                .updatedTime(comment.getUpdatedTime())
                .delYn(comment.getDelYn())
                .build();
    }
}
