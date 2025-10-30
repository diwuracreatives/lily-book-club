package com.lilybookclub.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lilybookclub.util.AppConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(message = "New Password is required")
    @Size(min = 4, max = 15, message = "New Password must be between 4 and 15 characters")
    @Pattern(regexp = AppConstant.PASSWORD_REGEX, message = "New Password must contain both letter and a number")
    private String newPassword;

    @NotBlank(message = "Confirm New Password is required")
    @Size(min = 4, max = 15, message = "Confirm New Password must be between 4 and 15 characters")
    @Pattern(regexp = AppConstant.PASSWORD_REGEX, message = "Confirm New must contain both letter and a number")
    private String confirmNewPassword;

    @JsonIgnore
    public String getNullableNewPassword() {
        return StringUtils.isBlank(newPassword) ? null : StringUtils.trim(newPassword);
    }

    @JsonIgnore
    public String getNullableConfirmNewPassword() {
        return StringUtils.isBlank(confirmNewPassword) ? null : StringUtils.trim(confirmNewPassword);
    }
}
