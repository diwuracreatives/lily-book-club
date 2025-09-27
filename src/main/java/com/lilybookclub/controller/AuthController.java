package com.lilybookclub.controller;

import com.lilybookclub.dto.request.user.LoginRequest;
import com.lilybookclub.dto.request.user.SignUpRequest;
import com.lilybookclub.dto.response.user.LoginResponse;
import com.lilybookclub.dto.response.user.SignUpResponse;
import com.lilybookclub.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/v1/auth")
@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for registering and log in")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an Account", description = "Add your details below to create account")
    public SignUpResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest){
        return authService.signUp(signUpRequest);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login into your Account", description = "Add your details below login your account")
    public LoginResponse login(@RequestBody @Valid LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
}
