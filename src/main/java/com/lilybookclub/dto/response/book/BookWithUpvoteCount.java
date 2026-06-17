package com.lilybookclub.dto.response.book;

import com.lilybookclub.entity.Book;

public interface BookWithUpvoteCount {
    Book getBook();
    Long getUpvoteCount();
    Long getDownvoteCount();
}
