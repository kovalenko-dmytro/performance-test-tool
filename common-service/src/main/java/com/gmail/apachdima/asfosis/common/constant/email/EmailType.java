package com.gmail.apachdima.asfosis.common.constant.email;

import java.util.Map;
import java.util.Set;

public enum EmailType {

    INVITE;

    private static final Map<EmailType, String> templates = Map.of(
        EmailType.INVITE, "invite-letter.html"
    );

    private static final Map<EmailType, Set<EmailTemplateProperty>> templateProperties = Map.of(
        EmailType.INVITE, Set.of(
            EmailTemplateProperty.recipientName, EmailTemplateProperty.text, EmailTemplateProperty.senderName
        )
    );

    public static String getTemplate(EmailType emailType) {
        return templates.get(emailType);
    }

    public static Set<EmailTemplateProperty> getTemplateProperties(EmailType emailType) {
        return templateProperties.get(emailType);
    }

    public enum EmailTemplateProperty {
        recipientName, text, senderName
    }
}
