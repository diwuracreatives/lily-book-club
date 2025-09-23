package com.lilybookclub.dto.request.club;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubActionByAdminRequest {
    @Positive(message = "User Id is required and must be a positive number")
    private long userId;
    @NotBlank(message = "Club category is required")
    private String category;
}
