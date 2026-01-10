package com.lilybookclub.controller;

import com.lilybookclub.dto.request.auth.*;
import com.lilybookclub.dto.response.auth.LoginResponse;
import com.lilybookclub.service.AuthService;
import com.lilybookclub.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for log in, reset password and auth management")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login into your Account", description = "Add your details below login your account")
    public LoginResponse login(@RequestBody @Valid LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Refresh Auth tokens", description = "Refresh Auth tokens")
    public LoginResponse refresh(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name="Bearer Authentication")
    @Operation(summary = "Log out of your Account", description = "Log out of your account")
    public void logout(){refreshTokenService.deleteRefreshToken();
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Forgot account password", description = "Forgot account password")
    public void forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest){
         authService.forgotPassword(forgotPasswordRequest);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name="Bearer Authentication")
    @Operation(summary = "Reset password", description = "Reset password")
    public void resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest){
        authService.resetPassword(resetPasswordRequest);
    }

    @PostMapping("/reset-password/user")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name="Bearer Authentication")
    @Operation(summary = "Reset password by logged in user", description = "Reset password by logged in user")
    public void resetPasswordByLoggedInUser(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        authService.resetPasswordByLoggedInUser(changePasswordRequest);
    }
}
