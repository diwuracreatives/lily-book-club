package com.lilybookclub.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lilybookclub.util.AppConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {

    @NotBlank(message = "Your Email is required")
    @Email(message = "Your Email must be valid email address", regexp = AppConstant.EMAIL_REGEX)
    private String email;

    @JsonIgnore
    public String getNullableEmail() {
        return StringUtils.isBlank(email) ? null : StringUtils.trim(email).toUpperCase();
    }
}
