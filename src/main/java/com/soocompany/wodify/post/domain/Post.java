package com.soocompany.wodify.post.domain;

import com.soocompany.wodify.common.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.post.dto.PostUpdateReqDto;
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
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 3000)
    private String contents;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void updatePost(PostUpdateReqDto postUpdateReqDto) {
        this.type = postUpdateReqDto.getType();
        this.title = postUpdateReqDto.getTitle();
        this.contents = postUpdateReqDto.getContents();
    }
}
