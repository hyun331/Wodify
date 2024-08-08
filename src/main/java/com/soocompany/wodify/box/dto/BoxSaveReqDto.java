package com.soocompany.wodify.box.dto;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoxSaveReqDto {
    private String name;
    private MultipartFile logoPath;
    private String operatingHours;
    private String fee;
    private String intro;
    private String address;
    private Long representativeId;



    public Box toEntity(Member member) {
        Box box = Box.builder()
                .name(this.name)
                .operatingHours(this.operatingHours)
                .fee(this.fee)
                .intro(this.intro)
                .address(this.address)
                .member(member)
                .build();

        box.updateBoxCode();

        return box;
    }

    public static BoxSaveReqDto fromEntity(Box box) {
        return BoxSaveReqDto.builder()
                .name(box.getName())
                .operatingHours(box.getOperatingHours())
                .fee(box.getFee())
                .intro(box.getIntro())
                .address(box.getAddress())
                .representativeId(box.getMember().getId())
                .build();
    }
}

