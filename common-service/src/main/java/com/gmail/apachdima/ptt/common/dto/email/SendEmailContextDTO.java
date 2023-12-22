package com.gmail.apachdima.ptt.common.dto.email;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Builder
public record SendEmailContextDTO(
    String to,
    Set<String> cc,
    String subject,
    String payload,
    MultipartFile[] attachments
) {
}
