package com.lilybookclub.dto.request.club;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lilybookclub.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateClubRequest {

    @NotBlank(message = "Club name is required")
    @Length(min = 3, max = 30, message = "Club name must be between 3 and 30 characters")
    private String name;

    @NotBlank(message = "Club code is required")
    @Length(min = 6, max = 6, message = "Club code must be 6 characters")
    private String code;

    @NotNull(message = "Club category is required")
    private Category category;

    @NotBlank(message = "Club description is required")
    @Length(min = 5, max = 250, message = "Club description must be between 5 and 250 characters ")
    private String description;

    @JsonIgnore
    public String getNullableName() {
        return StringUtils.isBlank(name) ? null : StringUtils.trim(name);
    }

    @JsonIgnore
    public String getNullableCode() {
        return StringUtils.isBlank(code) ? null : StringUtils.trim(code).toUpperCase();
    }

    @JsonIgnore
    public String getNullableDescription() {
        return StringUtils.isBlank(description) ? null : StringUtils.trim(description);
    }


}

