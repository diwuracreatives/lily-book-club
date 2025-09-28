package com.lilybookclub.service.impl;

import com.lilybookclub.entity.User;
import com.lilybookclub.enums.Role;
import com.lilybookclub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.firstname}")
    private String adminFirstname;

    @Value("${admin.lastname}")
    private String adminLastname;

    @EventListener(ApplicationReadyEvent.class)
    public void createDefaultAdminUser() {
        boolean adminAccountExist = userRepository.existsByEmail(adminEmail.toUpperCase());

        if (!adminAccountExist) {
             try {
                 String encodedPassword = passwordEncoder.encode(adminPassword);
                 User adminUser = User.builder()
                         .email(adminEmail.toUpperCase())
                         .firstname(adminFirstname)
                         .lastname(adminLastname)
                         .password(encodedPassword)
                         .role(Role.ADMIN)
                         .build();
                 userRepository.save(adminUser);
                 log.info("Created default admin user with email {}", adminEmail);
             } catch (Exception e) {
               log.error ("Error creating default admin account for {}", adminEmail, e);
             }
        } else {
            log.error ("A default admin account already exists with email {}", adminEmail);
        }


    }
}