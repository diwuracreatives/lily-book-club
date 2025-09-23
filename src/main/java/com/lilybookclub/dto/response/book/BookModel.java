package com.lilybookclub.dto.response.book;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookModel {
    private String title;
    private String author;
    private String link;
    private String imageUrl;
    private String description;
    private Long upvoteCount;
    private Long downvoteCount;
}
