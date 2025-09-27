package com.lilybookclub.mapper;

import com.lilybookclub.dto.response.book.BookModel;
import com.lilybookclub.entity.Book;
import com.lilybookclub.entity.BookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mappings({
            @Mapping(target = "upvoteCount", source = "upvoteCount"),
            @Mapping(target = "downvoteCount", source = "downvoteCount")
    })
    BookModel toResponse(Book book, Long upvoteCount, Long downvoteCount);

    BookModel toDto(BookRequest recommendedBook);

}
