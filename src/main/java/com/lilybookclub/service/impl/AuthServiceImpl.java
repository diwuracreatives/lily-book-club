package com.lilybookclub.service.impl;

import com.lilybookclub.dto.request.user.LoginRequest;
import com.lilybookclub.dto.response.user.LoginResponse;
import com.lilybookclub.exception.NotFoundException;
import com.lilybookclub.repository.UserRepository;
import com.lilybookclub.security.jwt.JwtService;
import com.lilybookclub.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public LoginResponse login(LoginRequest loginRequest){

        userRepository.findByEmail(loginRequest.getNullableEmail())
                .orElseThrow(() -> new NotFoundException("Account with email address not found"));

        return authenticateUser(loginRequest);

    }

    private LoginResponse authenticateUser(LoginRequest loginRequest) {
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getNullableEmail(), loginRequest.getNullablePassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getNullableEmail());
        String jwtToken = jwtService.generateToken(userDetails);

        return LoginResponse.builder()
                .token(jwtToken)
                .build();

    } catch (Exception ex) {
        log.error("Authentication failed for {}. {}", loginRequest.getNullableEmail(), ex.getMessage());
        throw new NotFoundException(ex.getMessage());
    }
    }


}


