package com.lilybookclub.service;

import com.lilybookclub.dto.request.book.CreateBookRequest;
import com.lilybookclub.dto.request.book.CreateClubBookRequest;
import com.lilybookclub.dto.response.book.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookModel addBookByAdmin(CreateBookRequest createBookRequest);
    String addBookToClub(CreateClubBookRequest createClubBookRequest);
    String removeBookFromClub(CreateClubBookRequest removeClubBookRequest);
    BookModel upvoteBook(Long bookId);
    BookModel downvoteBook(Long bookId);
    Page<BookModel> getBooks(Pageable pageable);
    BookModel recommendBook(CreateBookRequest recommendBookRequest, String clubCode);
    String approveRecommendedBook(Long bookId);
    String rejectRecommendedBook(Long bookId);
    void sendBookRecommendationEmail();
    Page<BookModel> getAllUpcomingBooks(String code, Pageable pageable);
}

