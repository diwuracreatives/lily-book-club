package com.lilybookclub.dto.request.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Length(min = 2, max = 250, message = "Book title must be between 3 and 50 characters")
    private String title;

    @NotBlank(message = "Book author is required")
    @Length(min = 1, max = 250, message = "Book author must be between 1 and 250 characters")
    private String author;

    @NotBlank(message = "Book link is required")
    @Length(min = 5, max = 150, message = "Book link must be between 5 and 150 characters")
    private String link;

    @NotBlank(message = "Book Image or Cover Url is required")
    @Length(min = 5, max = 250, message = "Book Image or Cover Url must be between 5 and 250 characters")
    private String imageUrl;

    @NotBlank(message = "Book Description is required")
    @Length(min = 5, max = 250, message = "Book Description must be between 5 and 250 characters")
    private String description;

    @JsonIgnore
    public String getNullableTitle() {
        return StringUtils.isBlank(title) ? null : StringUtils.trim(title);
    }

    @JsonIgnore
    public String getNullableAuthor() {
        return StringUtils.isBlank(author) ? null : StringUtils.trim(author);
    }

    @JsonIgnore
    public String getNullableLink() {
        return StringUtils.isBlank(link) ? null : StringUtils.trim(link);
    }

    @JsonIgnore
    public String getNullableImageUrl() {
        return StringUtils.isBlank(imageUrl) ? null : StringUtils.trim(imageUrl);
    }

    @JsonIgnore
    public String getNullableDescription() {
        return StringUtils.isBlank(description) ? null : StringUtils.trim(description);
    }
}
