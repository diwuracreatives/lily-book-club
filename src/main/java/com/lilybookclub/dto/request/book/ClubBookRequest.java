package com.lilybookclub.dto.request.book;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClubBookRequest {
    @Positive(message = "Club Id is required and must be a positive number")
    private long clubId;

    @Positive(message = "Book Id is required and must be a positive number")
    private long bookId;
}
