package com.lilybookclub.dto.request.book;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {
    @NotBlank(message = "Book title is required")
    private String title;

    @NotBlank(message = "Book Author is required")
    private String author;

    @NotBlank(message = "Book Link is required")
    private String link;

    @NotBlank(message = "Book Image or Cover Link is required")
    private String imageUrl;

    @NotBlank(message = "Book Description is required")
    private String description;
}
