package com.lilybookclub.dto.request.book;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {

    @NotBlank(message = "Book title is required")
    @Length(min = 3, max = 50, message = "Book title must be between 3 and 50 characters")
    private String title;

    @NotBlank(message = "Book author is required")
    @Length(min = 1, max = 60, message = "Book author must be between 1 and 60 characters")
    private String author;

    @NotBlank(message = "Book link is required")
    @Length(min = 5, max = 100, message = "Book link must be between 5 and 100 characters")
    private String link;

    @NotBlank(message = "Book Image or Cover Url is required")
    @Length(min = 5, max = 250, message = "Book Image or Cover Url must be at least 5 characters")
    private String imageUrl;

    @NotBlank(message = "Book Description is required")
    @Length(min = 5, max = 250, message = "Book Description must be at least 5 characters")
    private String description;

    public String getNullableTitle() {
        return StringUtils.isBlank(title) ? null : StringUtils.trim(title).toUpperCase();
    }

    public String getNullableAuthor() {
        return StringUtils.isBlank(author) ? null : StringUtils.trim(author).toUpperCase();
    }

    public String getNullableLink() {
        return StringUtils.isBlank(link) ? null : StringUtils.trim(link).toUpperCase();
    }

    public String getNullableImageUrl() {
        return StringUtils.isBlank(imageUrl) ? null : StringUtils.trim(imageUrl).toUpperCase();
    }

    public String getNullableDescription() {
        return StringUtils.isBlank(description) ? null : StringUtils.trim(description).toUpperCase();
    }
}
