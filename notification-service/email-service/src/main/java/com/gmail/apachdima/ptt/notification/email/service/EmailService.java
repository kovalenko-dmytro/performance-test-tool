package com.gmail.apachdima.ptt.notification.email.service;

import com.gmail.apachdima.ptt.common.dto.email.EmailRequestDTO;
import com.gmail.apachdima.ptt.common.dto.email.EmailResponseDTO;
import com.gmail.apachdima.ptt.common.dto.email.EmailTemplatePropertiesResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

public interface EmailService {
    EmailTemplatePropertiesResponseDTO getEmailTemplateProperties();
    EmailResponseDTO send(EmailRequestDTO request, MultipartFile[] attachment, Locale locale);
}
