package com.lilybookclub.dto.response.book;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookModel {

    private String title;

    private String author;

    private String link;

    private String imageUrl;

    private String description;

    private Long upvoteCount;

    private Long downvoteCount;
}
