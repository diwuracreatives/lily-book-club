package com.lilybookclub.service.impl;

import com.lilybookclub.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${mail.name}")
    private String mailName;

    @Async
    public void sendMail(String mailTo, String mailSubject, String templateLocation, Map<String, Object> params)  {
        Context context = new Context();
        context.setVariables(params);

        String emailContent = templateEngine.process("mail/" + templateLocation, context);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(mailFrom, mailName);
        messageHelper.setTo(mailTo);
        messageHelper.setSubject(mailSubject);
        messageHelper.setText(emailContent, true);

        };
    try {
        mailSender.send(messagePreparator);
        log.info("Email successfully sent to {}", mailTo);
    } catch (MailException e) {
        log.error(" Failed to send email to {}: {}", mailTo, e.getMessage(), e);
    }
    }
}
