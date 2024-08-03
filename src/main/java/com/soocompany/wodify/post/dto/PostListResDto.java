package com.soocompany.wodify.post.dto;

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
    private String memberEmail;

    public PostListResDto listFromEntity(Post post) {
        return PostListResDto.builder()
                .id(post.getId())
                .type(post.getType())
                .title(post.getTitle())
                .memberEmail(post.getMember().getEmail())
                .build();
    }

}
