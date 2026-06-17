package com.lilybookclub.service;

import com.lilybookclub.dto.request.user.SignUpRequest;
import com.lilybookclub.dto.response.user.SignUpResponse;

public interface UserService {
    SignUpResponse signUp(SignUpRequest signUpRequest);
}
