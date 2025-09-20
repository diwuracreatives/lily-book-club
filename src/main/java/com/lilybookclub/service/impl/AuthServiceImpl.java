package com.lilybookclub.service.impl;

import com.lilybookclub.dto.request.user.LoginRequest;
import com.lilybookclub.dto.request.user.SignUpRequest;
import com.lilybookclub.dto.response.user.LoginResponse;
import com.lilybookclub.dto.response.user.SignUpResponse;
import com.lilybookclub.entity.User;
import com.lilybookclub.enums.Role;
import com.lilybookclub.exception.BadRequestException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest){
          boolean userAccountExist = userRepository.existsByEmail(signUpRequest.getEmail().trim());

          if (userAccountExist){
              throw new BadRequestException("An account with this email address already exists");
          }

          String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword().trim());

          User user = User.builder()
                .email(signUpRequest.getEmail())
                .firstname(signUpRequest.getFirstname().trim())
                .lastname(signUpRequest.getLastname().trim())
                .password(encodedPassword)
                .role(Role.USER)
                .build();
          userRepository.save(user);

           return SignUpResponse.builder()
                .email(user.getEmail())
                .firstname(user.getFirstname())
                   .lastname(user.getLastname())
                .build();

    }

    @Override
    public LoginResponse login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail().trim())
                .orElseThrow(() -> new NotFoundException("Account with email address not found"));

        return authenticateUser(loginRequest, user);
    }

    private LoginResponse authenticateUser(LoginRequest loginRequest, User user) {
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);

        return LoginResponse.builder()
                .token(jwtToken)
                .build();

    } catch (Exception ex) {
        log.error("Authentication failed for {}. {}", loginRequest.getEmail(), ex.getMessage());
        throw new NotFoundException(ex.getMessage());
    }
    }


}


