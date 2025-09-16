package com.lilybookclub.dto.request.club;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubActionByUserRequest {
    @NotBlank(message = "Club code is required")
    private String clubCode;
}
