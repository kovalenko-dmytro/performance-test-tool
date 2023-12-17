package com.gmail.apachdima.asfosis.notification.email.controller;

import com.gmail.apachdima.asfosis.common.dto.email.EmailRequestDTO;
import com.gmail.apachdima.asfosis.common.dto.email.EmailResponseDTO;
import com.gmail.apachdima.asfosis.common.dto.email.EmailTemplatePropertiesResponseDTO;
import com.gmail.apachdima.asfosis.notification.email.service.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@Tag(name = "Email Service REST API")
@RestController
@RequestMapping(value = "/api/v1/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping(value = "/template-properties")
    public ResponseEntity<EmailTemplatePropertiesResponseDTO> getEmailTemplateProperties() {
        return ResponseEntity.ok(emailService.getEmailTemplateProperties());
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<EmailResponseDTO> send(
        @RequestPart(value = "emailRequest") EmailRequestDTO emailRequest,
        @RequestPart(value = "attachments", required = false) MultipartFile[] attachments,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        return ResponseEntity.ok(emailService.send(emailRequest, attachments, locale));
    }
}
