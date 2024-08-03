package com.soocompany.wodify.post.domain;

import com.soocompany.wodify.common.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.post.dto.CommentUpdateReqDto;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 3000)
    private String comment;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateComment(CommentUpdateReqDto commentUpdateReqDto) {
        comment = commentUpdateReqDto.getComment();
    }
}
