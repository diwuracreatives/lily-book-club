package com.lilybookclub.dto.request.user;

import com.lilybookclub.util.AppConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Your Email is required")
    @Email(message = "Your Email must be valid email address", regexp = AppConstant.EMAIL_REGEX)
    private String email;

    @NotBlank(message = "Your Password is required")
    @Size(min = 4, max = 15, message = "Your Password must be between 4 and 15 characters")
    @Pattern(regexp = AppConstant.PASSWORD_REGEX, message = "Your Password must contain both letter and a number")
    private String password;

}
