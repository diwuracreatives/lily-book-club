package com.lilybookclub.dto.response.book;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookModel {

    private String title;

    private String author;

    private String link;

    private String imageUrl;

    private String description;

    private long upvoteCount;

    private long downvoteCount;
}
