package com.lilybookclub.service;

import com.lilybookclub.dto.request.LoginRequest;
import com.lilybookclub.dto.request.SignUpRequest;
import com.lilybookclub.dto.response.LoginResponse;
import com.lilybookclub.dto.response.SignUpResponse;

public interface AuthService {
     SignUpResponse signUp(SignUpRequest signUpRequest);
     LoginResponse login(LoginRequest loginRequest);
}
