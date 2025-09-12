package com.lilybookclub.security.jwt;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class CustomAuditorAware implements AuditorAware<String> {
    private static final String DEFAULT_SYSTEM_USER = "SYSTEM";

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken token) {
            return Optional.of(token.getName());
        }
        return Optional.of(DEFAULT_SYSTEM_USER);
    }

}
