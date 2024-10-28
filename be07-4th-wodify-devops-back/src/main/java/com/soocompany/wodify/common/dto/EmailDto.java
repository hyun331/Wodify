package com.soocompany.wodify.common.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {
    private String receiverEmail;
    private String emailTitle;
    private String emailContent;
}
