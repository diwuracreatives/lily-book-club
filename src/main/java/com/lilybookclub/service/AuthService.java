package com.lilybookclub.service;

import com.lilybookclub.dto.request.user.LoginRequest;
import com.lilybookclub.dto.request.user.SignUpRequest;
import com.lilybookclub.dto.response.user.LoginResponse;
import com.lilybookclub.dto.response.user.SignUpResponse;

public interface AuthService {
     SignUpResponse signUp(SignUpRequest signUpRequest);
     LoginResponse login(LoginRequest loginRequest);
}
