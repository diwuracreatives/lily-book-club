package com.lilybookclub.service;

import com.lilybookclub.dto.request.auth.*;
import com.lilybookclub.dto.response.auth.LoginResponse;


public interface AuthService {
     LoginResponse login(LoginRequest loginRequest);
     LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
     void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
     void resetPassword(ResetPasswordRequest resetPasswordRequest);
     void resetPasswordByLoggedInUser(ChangePasswordRequest changePasswordRequest);
}
