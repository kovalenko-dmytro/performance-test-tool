package com.gmail.apachdima.ptt.common.dto.email;

import com.gmail.apachdima.ptt.common.constant.email.EmailStatus;
import com.gmail.apachdima.ptt.common.constant.email.EmailType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EmailResponseDTO(
    String emailId,
    String sendBy,
    String sendTo,
    EmailType emailType,
    EmailStatus emailStatus,
    LocalDateTime sendTime
) {
}
