package com.gmail.apachdima.asfosis.common.dto.email;

import com.gmail.apachdima.asfosis.common.constant.email.EmailType;
import lombok.Builder;

import java.util.Map;
import java.util.Set;

@Builder
public record EmailTemplatePropertiesResponseDTO(
    Map<EmailType, Set<EmailType.EmailTemplateProperty>> emailTemplateProperties
) {
}
