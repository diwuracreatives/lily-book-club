package com.lilybookclub.dto.request.club;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubInterestRequest {

    @NotBlank(message = "Interest story is required")
    @Length(min = 5, message = "Interest story must have at least 5 characters")
    @Size(max = 200, message = "Interest story is too long")
    private String interest;

    @JsonIgnore
    public String getNullableInterest() {
        return StringUtils.isBlank(interest) ? null : StringUtils.trim(interest);
    }
}
