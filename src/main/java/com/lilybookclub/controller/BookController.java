package com.lilybookclub.controller;

import com.lilybookclub.dto.request.book.CreateBookRequest;
import com.lilybookclub.dto.request.book.CreateClubBookRequest;
import com.lilybookclub.dto.response.book.BookModel;
import com.lilybookclub.service.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/v1/books")
@RestController
@RequiredArgsConstructor
@Tag(name = "Book Management", description = "Endpoints for managing book by user and admin")
@SecurityRequirement(name="Bearer Authentication")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<BookModel> getBooks(@PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        return bookService.getBooks(pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public BookModel createBook(@RequestBody @Valid CreateBookRequest createBookRequest) {
        return bookService.addBookByAdmin(createBookRequest);
    }

    @GetMapping("club/{clubCode}")
    @ResponseStatus(HttpStatus.OK)
    public Page<BookModel> get(@PathVariable String clubCode, @PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        return bookService.getAllUpcomingBooks(clubCode, pageable);
    }

    @PostMapping("/club")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String>  addBookToClub(@RequestBody @Valid CreateClubBookRequest createClubBookRequest) {
        String message = bookService.addBookToClub(createClubBookRequest);
        return Map.of("message", message);
    }

    @DeleteMapping("/club")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> removeBookFromClub(@RequestBody @Valid CreateClubBookRequest createClubBookRequest) {
        String message = bookService.removeBookFromClub(createClubBookRequest);
        return Map.of("message", message);
    }

    @PostMapping("/{bookId}/upvote")
    @ResponseStatus(HttpStatus.OK)
    public BookModel upvoteBook(@PathVariable Long bookId) {
        return bookService.upvoteBook(bookId);
    }

    @PostMapping("/{bookId}/downvote")
    @ResponseStatus(HttpStatus.OK)
    public BookModel downvoteBook(@PathVariable Long bookId) {
        return bookService.downvoteBook(bookId);
    }

    @PostMapping("/recommendations/{clubCode}")
    @ResponseStatus(HttpStatus.OK)
    public BookModel recommendBook(@RequestBody @Valid CreateBookRequest createBookRequest, @PathVariable String clubCode) {
        return bookService.recommendBook(createBookRequest, clubCode);
    }

    @PostMapping("/{bookId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> approveRecommendedBook(@PathVariable Long bookId) {
        String message =  bookService.approveRecommendedBook(bookId);
        return Map.of("message", message);
    }

    @PostMapping("/{bookId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> rejectRecommendedBook(@PathVariable Long bookId) {
        String message =  bookService.rejectRecommendedBook(bookId);
        return Map.of("message", message);
    }

}
