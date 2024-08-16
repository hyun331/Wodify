package com.soocompany.wodify.post.dto;

import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.domain.Type;
import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateReqDto {

    @Enumerated(EnumType.STRING)
    private Type type;
    private String title;
    private String contents;
    private List<Long> removedFileIds;
    private MultipartFile[] newFiles;
}
