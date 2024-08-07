package com.soocompany.wodify.post.domain;

import com.soocompany.wodify.common.domain.BaseEntity;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.post.dto.PostUpdateReqDto;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 3000)
    private String contents;

    @Builder.Default
    @Column(nullable = false)
    private Long likeCount = 0L;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Image> files = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void updatePost(PostUpdateReqDto postUpdateReqDto) {
        this.type = postUpdateReqDto.getType();
        this.title = postUpdateReqDto.getTitle();
        this.contents = postUpdateReqDto.getContents();
    }

    public void deletePost() {
        this.updateDelYn();
    }

    public Long updateLikeCount(Long one) {
        this.likeCount += one;
        return this.likeCount;
    }
}
