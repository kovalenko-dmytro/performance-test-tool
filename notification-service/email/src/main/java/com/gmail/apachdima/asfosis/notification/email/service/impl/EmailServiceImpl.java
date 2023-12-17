package com.gmail.apachdima.asfosis.notification.email.service.impl;

import com.gmail.apachdima.asfosis.common.constant.common.CommonConstant;
import com.gmail.apachdima.asfosis.common.constant.email.EmailStatus;
import com.gmail.apachdima.asfosis.common.constant.email.EmailType;
import com.gmail.apachdima.asfosis.common.constant.message.Error;
import com.gmail.apachdima.asfosis.common.dto.email.EmailRequestDTO;
import com.gmail.apachdima.asfosis.common.dto.email.EmailResponseDTO;
import com.gmail.apachdima.asfosis.common.dto.email.EmailTemplatePropertiesResponseDTO;
import com.gmail.apachdima.asfosis.common.dto.email.SendEmailContextDTO;
import com.gmail.apachdima.asfosis.common.exception.AFSApplicationException;
import com.gmail.apachdima.asfosis.notification.email.mapper.EmailMapper;
import com.gmail.apachdima.asfosis.notification.email.model.EmailLog;
import com.gmail.apachdima.asfosis.notification.email.repository.EmailLogRepository;
import com.gmail.apachdima.asfosis.notification.email.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailLogRepository emailLogRepository;
    private final EmailMapper emailMapper;
    private final MessageSource messageSource;

    @Value("${application.default-admin.email}")
    private String adminEmail;

    @Override
    public EmailTemplatePropertiesResponseDTO getEmailTemplateProperties() {
        Map<EmailType, Set<EmailType.EmailTemplateProperty>> emailTemplateProperties = new HashMap<>();
        Arrays.stream(EmailType.values()).forEach(emailType ->
            emailTemplateProperties.put(emailType, EmailType.getTemplateProperties(emailType))
        );
        return EmailTemplatePropertiesResponseDTO.builder()
            .emailTemplateProperties(emailTemplateProperties)
            .build();
    }

    @Override
    public EmailResponseDTO send(EmailRequestDTO request, MultipartFile[] attachments, Locale locale) {
        Context templateContext = new Context(locale);
        templateContext.setVariables(request.properties());
        String template = EmailType.getTemplate(request.emailType());
        if (Objects.isNull(template)) {
            throw new AFSApplicationException(
                messageSource.getMessage(
                    Error.EMAIL_TEMPLATE_NOT_FOUND.getKey(), new Object[]{request.emailType()}, locale));
        }
        String payload = templateEngine.process(template, templateContext);

        SendEmailContextDTO sendEmailContext = SendEmailContextDTO.builder()
            .to(request.to())
            .cc(request.cc())
            .subject(request.subject())
            .payload(payload)
            .attachments(attachments)
            .build();
        sendPreparedEmail(sendEmailContext, locale);
        EmailLog emailLog = emailLogRepository.save(buildEmailLog(request));
        return emailMapper.toEmailResponseDTO(emailLog);
    }

    private void sendPreparedEmail(SendEmailContextDTO context, Locale locale) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, CommonConstant.DEFAULT_CHARSET.getValue());
            helper.setFrom(adminEmail);
            helper.setTo(context.to());
            if (CollectionUtils.isNotEmpty(context.cc())) {
                helper.setCc(context.cc().toArray(String[]::new));
            }
            helper.setSubject(context.subject());
            helper.setText(context.payload(), true);
            if (Objects.nonNull(context.attachments())) {
                addAttachments(helper, context.attachments());
            }
            emailSender.send(message);
        } catch (MessagingException | IOException e) {
            Object[] params = new Object[]{context.to(), e.getMessage()};
            throw new AFSApplicationException(messageSource.getMessage(Error.EMAIL_SEND_ERROR.getKey(), params, locale));
        }
    }

    private EmailLog buildEmailLog(EmailRequestDTO request) {
        return EmailLog.builder()
            .sendBy(SecurityContextHolder.getContext().getAuthentication().getName())
            .sendTo(request.to())
            .emailType(request.emailType())
            .emailStatus(EmailStatus.SUCCESS)
            .sendTime(LocalDateTime.now())
            .build();
    }

    private void addAttachments(
        MimeMessageHelper helper,
        MultipartFile[] attachments
    ) throws MessagingException, IOException {
        for (MultipartFile attachment : attachments) {
            if (Objects.nonNull(attachment)) {
                helper.addAttachment(Objects.requireNonNull(attachment.getOriginalFilename()), attachment);
            }
        }
    }
}
