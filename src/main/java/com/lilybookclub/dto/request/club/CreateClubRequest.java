package com.lilybookclub.dto.request.club;

import com.lilybookclub.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClubRequest {
    @NotBlank(message = "Club name is required")
    @Size(min = 3, max = 12, message = "Club name must be between 3 and 12 characters")
    private String name;

    @NotBlank(message = "Club code is required")
    @Size(min = 3, max = 12, message = "Club code must be between 3 and 12 characters")
    private String code;

    @NotNull(message = "Club category is required")
    private Category category;

    @NotBlank(message = "Club description is required")
    @Size(min = 5, message = "Club description must be at least 5 characters")
    private String description;


    public String getNullableName() {
        return StringUtils.isBlank(name) ? null : StringUtils.trim(name).toUpperCase();
    }

    public String getNullableCode() {
        return StringUtils.isBlank(code) ? null : StringUtils.trim(code).toUpperCase();
    }

    public String getNullableDescription() {
        return StringUtils.isBlank(description) ? null : StringUtils.trim(description).toUpperCase();
    }
}

