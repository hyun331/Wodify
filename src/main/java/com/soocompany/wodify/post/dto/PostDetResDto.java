package com.soocompany.wodify.post.dto;

import com.soocompany.wodify.post.domain.Comment;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetResDto {
    private Long id;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String title;
    private String contents;
    private String memberEmail;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    List<CommentResDto> comments;

    static public PostDetResDto fromEntity(Post post) {
        return PostDetResDto.builder()
                .id(post.getId())
                .type(post.getType())
                .title(post.getTitle())
                .contents(post.getContents())
                .memberEmail(post.getMember().getEmail())
                .createdTime(post.getCreatedTime())
                .updatedTime(post.getUpdatedTime())
                .comments(post.getComments().stream()
                        .filter(comment -> comment.getDelYn().equals("N"))
                        .map(CommentResDto::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
