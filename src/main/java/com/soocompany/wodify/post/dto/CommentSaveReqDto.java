package com.soocompany.wodify.post.dto;

import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.post.domain.Comment;
import com.soocompany.wodify.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentSaveReqDto {

    private Long postId;
    private String comment;
    private String parentId;

    public Comment toEntity(Member member, Post post, Comment parentComment) {
        return Comment.builder()
                .comment(this.comment)
                .post(post)
                .member(member)
                .parent(parentComment)
                .build();
    }
}
