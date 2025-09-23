package com.lilybookclub.service;

import com.lilybookclub.dto.request.book.CreateBookRequest;
import com.lilybookclub.dto.request.book.ClubBookRequest;
import com.lilybookclub.dto.request.book.RecommendBookRequest;
import com.lilybookclub.dto.response.book.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookModel addBookByAdmin(CreateBookRequest createBookRequest);
    String addBookToClub(ClubBookRequest createClubBookRequest);
    String removeBookFromClub(ClubBookRequest removeClubBookRequest);
    BookModel upvoteBook(Long bookId);
    BookModel downvoteBook(Long bookId);
    Page<BookModel> getBooks(Pageable pageable);
    BookModel recommendBook(RecommendBookRequest recommendBookRequest);
    String approveRecommendedBook(Long bookId);
    String rejectRecommendedBook(Long bookId);
    void sendBookRecommendationEmail();
}

