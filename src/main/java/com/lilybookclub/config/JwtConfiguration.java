package com.lilybookclub.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
@NoArgsConstructor
public class JwtConfiguration {
    private String secretKey;
    private int expiryDuration;
    private String AuthorizationHeader = HttpHeaders.AUTHORIZATION;
}

