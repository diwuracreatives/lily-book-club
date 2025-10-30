package com.lilybookclub.dto.request.auth;

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
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh Token is required")
    private String token;

    @JsonIgnore
    public String getNullableToken() {
        return StringUtils.isBlank(token) ? null : StringUtils.trim(token);
    }
}
