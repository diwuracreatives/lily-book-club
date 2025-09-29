package com.lilybookclub.service;

import com.lilybookclub.dto.request.user.LoginRequest;
import com.lilybookclub.dto.response.user.LoginResponse;

public interface AuthService {
     LoginResponse login(LoginRequest loginRequest);
}
