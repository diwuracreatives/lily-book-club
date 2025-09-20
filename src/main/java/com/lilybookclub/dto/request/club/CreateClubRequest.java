package com.lilybookclub.dto.request.club;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClubRequest {
    @NotBlank(message = "Club name is required")
    @Size(min = 3, max = 12, message = "Club name must be between 3 and 12 characters")
    private String name;

    @NotBlank(message = "Club category is required")
    private String category;

    @NotBlank(message = "Club description is required")
    @Size(min = 5, message = "Club description must be at least 5 characters")
    private String description;
}

