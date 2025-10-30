package com.lilybookclub.service.impl;

import com.lilybookclub.dto.request.auth.*;
import com.lilybookclub.dto.response.auth.LoginResponse;
import com.lilybookclub.entity.RefreshToken;
import com.lilybookclub.entity.User;
import com.lilybookclub.exception.BadRequestException;
import com.lilybookclub.exception.NotFoundException;
import com.lilybookclub.repository.UserRepository;
import com.lilybookclub.security.UserDetailsServiceImpl;
import com.lilybookclub.security.jwt.JwtService;
import com.lilybookclub.service.AuthService;
import com.lilybookclub.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    private User checkIfUserExists(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Account with email address not found"));
    }

    private String loadUserAndCreateToken(String email){
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return jwtService.generateToken(userDetails);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest){
        User user = checkIfUserExists(loginRequest.getEmail());
        return authenticateUser(loginRequest, user);
    }

    private LoginResponse authenticateUser(LoginRequest loginRequest, User user) {
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getNullableEmail(), loginRequest.getNullablePassword()));

        String jwtToken = loadUserAndCreateToken((loginRequest.getNullableEmail()));
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return LoginResponse.builder()
                .jwtToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    } catch (Exception ex) {
        log.error("Authentication failed for {}. {}", loginRequest.getNullableEmail(), ex.getMessage());
        throw new NotFoundException(ex.getMessage());
    }
    }


    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        RefreshToken token = refreshTokenService.isRefreshTokenValid(refreshTokenRequest.getNullableToken());
        log.info("Refresh token valid: {}", refreshTokenRequest.getNullableToken());

        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getUser().getEmail());
        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = refreshTokenService.resetRefreshToken(token);

        return LoginResponse.builder()
                .jwtToken(jwtToken)
                .refreshToken(refreshToken)
                .build();

    }

    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        log.info("In Forgot password request method");
        Optional<User> optionalUser = userRepository.findByEmail(forgotPasswordRequest.getNullableEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String jwtToken = loadUserAndCreateToken(user.getEmail());

            Map<String, Object> params = new HashMap<>();
            params.put("firstname", user.getFirstname());
            params.put("token", jwtToken);
            emailService.sendMail(user.getEmail(), "Password Reset", "reset-password", params);
        }
    }

    private void checkIfPasswordMatches(String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new BadRequestException("Invalid Credentials");
        }
    }

    private void saveNewPassword(String newPassword, User user) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("New password has been saved for {}", user.getEmail());
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        checkIfPasswordMatches(resetPasswordRequest.getNullableNewPassword(), resetPasswordRequest.getNullableConfirmNewPassword());

        User user = userDetailsService.getLoggedInUser();

        saveNewPassword(resetPasswordRequest.getNullableNewPassword(), user);
    }

    public void resetPasswordByLoggedInUser(ChangePasswordRequest changePasswordRequest) {

        checkIfPasswordMatches(changePasswordRequest.getNullableNewPassword(), changePasswordRequest.getNullableConfirmNewPassword());

        User user = userDetailsService.getLoggedInUser();
        String encodedOldPassword = passwordEncoder.encode(changePasswordRequest.getNullableOldPassword());

        if (!passwordEncoder.matches(changePasswordRequest.getNullableOldPassword(), user.getPassword())) {
            log.info("Old Password {} does not match saved {}", encodedOldPassword, user.getPassword());
            throw new BadRequestException("Invalid Credentials");
        }

        saveNewPassword(changePasswordRequest.getNullableNewPassword(), user);
    }

    // rate limiting, deploy

}


