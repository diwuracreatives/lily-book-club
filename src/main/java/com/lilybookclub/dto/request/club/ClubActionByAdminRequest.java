package com.lilybookclub.dto.request.club;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubActionByAdminRequest {

    @Positive(message = "User Id is required and must be a positive number")
    private long userId;

    @NotBlank(message = "Club code is required")
    @Length(min = 6, max = 6, message = "Club code must be 6 characters")
    private String clubCode;

    public String getNullableCode() {
        return StringUtils.isBlank(clubCode) ? null : StringUtils.trim(clubCode).toUpperCase();
    }


}
