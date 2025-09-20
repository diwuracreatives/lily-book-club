package com.lilybookclub.dto.request.club;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubActionByAdminRequest {
    @NotNull(message = "User Id is required")
    private long userId;
    @NotBlank(message = "Club category is required")
    private String category;
}
