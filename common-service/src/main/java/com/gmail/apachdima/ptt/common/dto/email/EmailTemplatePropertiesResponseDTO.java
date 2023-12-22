package com.gmail.apachdima.ptt.common.dto.email;

import com.gmail.apachdima.ptt.common.constant.email.EmailType;
import lombok.Builder;

import java.util.Map;
import java.util.Set;

@Builder
public record EmailTemplatePropertiesResponseDTO(
    Map<EmailType, Set<EmailType.EmailTemplateProperty>> emailTemplateProperties
) {
}
