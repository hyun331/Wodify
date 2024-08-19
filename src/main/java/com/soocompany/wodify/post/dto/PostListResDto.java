package com.soocompany.wodify.post.dto;

import com.soocompany.wodify.post.domain.DateTimeFormatterUtil;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListResDto {
    private Long id;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String title;
    private Long likeCount;
    private String name;
    private String createdTime;
    private int commentCount;

    static public PostListResDto fromEntity(Post post) {
        return PostListResDto.builder()
                .id(post.getId())
                .type(post.getType())
                .title(post.getTitle())
                .likeCount(post.getLikeCount())
                .name(post.getMemberName())
                .createdTime(DateTimeFormatterUtil.dateOnly(post.getCreatedTime()))
                .commentCount(post.getComments().size())
                .build();
    }
}
