package com.lilybookclub.service.impl;

import com.lilybookclub.service.EmailService;
import com.lilybookclub.util.AppUtil;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;


    @Value("${spring.mail.username}")
    private String mailFrom;

    @Value("${mail.name}")
    private String mailName;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${resend.api.key}")
    private String resendApiKey;

     private Resend resend;

    @Async
    public void sendMail(String mailTo, String mailSubject, String templateLocation, Map<String, Object> params)  {

        if (resend == null) {
           resend = new Resend(resendApiKey);
        }

        Context context = new Context();
        context.setVariable("greeting", AppUtil.getTimeGreeting());
        context.setVariable("baseUrl", baseUrl);
        context.setVariables(params);

        String emailContent = templateEngine.process("mail/" + templateLocation, context);

        CreateEmailOptions createEmailOptions = CreateEmailOptions.builder()
                .from(mailName + " <" + mailFrom + ">")
                .to(mailTo.toLowerCase().trim())
                .subject(mailSubject)
                .html(emailContent)
                .build();
        try {
            CreateEmailResponse data = resend.emails().send(createEmailOptions);
            log.info("Email successfully sent to {}", mailTo);
            System.out.print(data.getId());
        } catch (ResendException e) {
            log.error(" Failed to send email to {}: {}", mailTo, e.getMessage(), e);
        }
    }
}