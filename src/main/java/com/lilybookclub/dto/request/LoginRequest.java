package com.lilybookclub.dto.request;

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

    @NotBlank(message = "Dear Lily, Your Email is required")
    @Email(message = "Dear lily, Your Email must be valid email address", regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w+)+$")
    private String email;

    @NotBlank(message = "Dear Lily, Your Password is required")
    @Size(min = 4, max = 15, message = "Dear Lily, Your Password must be between 4 and 15 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "Dear Lily, Your Password must contain both letter and a number")
    private String password;

}
