package com.lilybookclub.service.impl;

import com.lilybookclub.dto.request.LoginRequest;
import com.lilybookclub.dto.request.SignUpRequest;
import com.lilybookclub.dto.response.LoginResponse;
import com.lilybookclub.dto.response.SignUpResponse;
import com.lilybookclub.entity.User;
import com.lilybookclub.enums.Role;
import com.lilybookclub.exception.BadRequestException;
import com.lilybookclub.exception.UserNotFoundException;
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
          Boolean userAccountExist = userRepository.existsByEmail(signUpRequest.getEmail());

          if (userAccountExist){
              throw new BadRequestException("Dear Lily, An account with this email address already exists");
          }

          String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

          User user = new User();
          user.setEmail(signUpRequest.getEmail());
          user.setFirstname(signUpRequest.getFirstname());
          user.setLastname(signUpRequest.getLastname());
          user.setPassword(encodedPassword);
          user.setRole(Role.USER);

          userRepository.save(user);

           return SignUpResponse.builder()
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .build();

    }

    @Override
    public LoginResponse login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("Account with email address not found"));

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
        throw new UserNotFoundException(ex.getMessage());
    }
    }


}


