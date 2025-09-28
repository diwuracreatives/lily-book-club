package com.lilybookclub.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lilybookclub.util.AppConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {

    @NotBlank(message = "Your Email is required")
    @Email(message = "Your Email must be valid email address", regexp = AppConstant.EMAIL_REGEX)
    private String email;

    @NotBlank(message = "Your Password is required")
    @Size(min = 4, max = 15, message = "Your Password must be between 4 and 15 characters")
    @Pattern(regexp = AppConstant.PASSWORD_REGEX, message = "Your Password must contain both letter and a number")
    private String password;

    @NotBlank(message = "Your first name is required")
    @Size(max = 250, message = "First name is too long")
    private String firstname;

    @NotBlank(message = "Your last name is required")
    @Size(max = 250, message = "Last name is too long")
    private String lastname;

    @JsonIgnore
    public String getNullableEmail() {
        return StringUtils.isBlank(email) ? null : StringUtils.trim(email).toUpperCase();
    }

    @JsonIgnore
    public String getNullablePassword() {
        return StringUtils.isBlank(password) ? null : StringUtils.trim(password);
    }

    @JsonIgnore
    public String getNullableFirstname() {
        return StringUtils.isBlank(firstname) ? null : StringUtils.trim(firstname);
    }

    @JsonIgnore
    public String getNullableLastname() {
        return StringUtils.isBlank(lastname) ? null : StringUtils.trim(lastname);
    }

}

