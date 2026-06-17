package com.lilybookclub.config;

import com.lilybookclub.dto.request.user.SignUpRequest;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfiguration {

    public List<SignUpRequest> adminUsers() {
        return List.of(
                new SignUpRequest("admin@gmail.com", "admin2025", "admin", "admin"));
    }

}
