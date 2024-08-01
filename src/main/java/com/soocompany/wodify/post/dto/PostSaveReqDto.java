package com.soocompany.wodify.post.dto;

import com.soocompany.wodify.member.domain.Member;
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
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveReqDto {

    private String title;
    private String contents;
    @Enumerated(EnumType.STRING)
    private Type type;

    public Post toEntity(Member member) {
        return Post.builder()
                .title(this.title)
                .contents(this.contents)
                .type(this.type)
                .member(member)
                .build();
    }
}
