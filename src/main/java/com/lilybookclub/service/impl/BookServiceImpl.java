package com.lilybookclub.service.impl;

import com.lilybookclub.dto.request.book.CreateClubBookRequest;
import com.lilybookclub.dto.request.book.CreateBookRequest;
import com.lilybookclub.dto.response.book.BookModel;
import com.lilybookclub.dto.response.book.BookWithUpvoteCount;
import com.lilybookclub.entity.*;
import com.lilybookclub.enums.BookApprovalStatus;
import com.lilybookclub.enums.DayOfTheWeek;
import com.lilybookclub.enums.Vote;
import com.lilybookclub.exception.BadRequestException;
import com.lilybookclub.exception.NotFoundException;
import com.lilybookclub.mapper.BookMapper;
import com.lilybookclub.repository.*;
import com.lilybookclub.security.UserDetailsServiceImpl;
import com.lilybookclub.service.BookService;
import com.lilybookclub.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

      private final BookRepository bookRepository;
      private final ClubRepository clubRepository;
      private final ClubBookRepository clubBookRepository;
      private final UserClubRepository userClubRepository;
      private final BookVoteRepository bookVoteRepository;
      private final UserDetailsServiceImpl userDetailsService;
      private final BookRequestRepository bookRequestRepository;
      private final EmailService emailService;
      private final BookMapper bookMapper;


    private Book checkIfBookExists(Long bookId){
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book with this id not found"));
     }

    private Club checkIfClubExists(String clubCode){
        return clubRepository.findByCode(clubCode)
                .orElseThrow(() -> new NotFoundException("Club with this code not found"));
    }

    private Book createBook(CreateBookRequest createBookRequest){
         Book book = Book.builder()
                 .title(createBookRequest.getNullableTitle())
                 .author(createBookRequest.getNullableAuthor())
                 .link(createBookRequest.getNullableLink())
                 .imageUrl(createBookRequest.getNullableImageUrl())
                 .description(createBookRequest.getNullableDescription())
                 .build();
         bookRepository.save(book);
         return book;
     }

     private void createClubBook(Long bookId, String clubCode){

         ClubBook clubBook = ClubBook.builder()
                 .book(checkIfBookExists(bookId))
                 .club(checkIfClubExists(clubCode))
                 .build();
         clubBookRepository.save(clubBook);
     }

    @Override
     public BookModel addBookByAdmin(CreateBookRequest createBookRequest){
           Book book = createBook(createBookRequest);

           return bookMapper.toResponse(book, null, null);
     }

    @Override
     public String addBookToClub(CreateClubBookRequest createClubBookRequest){
        createClubBook(createClubBookRequest.getBookId(), createClubBookRequest.getNullableCode());
        return String.format("Book: %s successfully added to club: %s", createClubBookRequest.getBookId(), createClubBookRequest.getNullableCode());
     }

    @Override
     public String removeBookFromClub(CreateClubBookRequest removeClubBookRequest){

          Book book = checkIfBookExists(removeClubBookRequest.getBookId());
          Club club = checkIfClubExists(removeClubBookRequest.getNullableCode());

          Optional<ClubBook> clubBook = clubBookRepository.findByClubAndBook(club, book);

          if (clubBook.isPresent()) {
              clubBook.get().setIsDeleted(true);
              clubBookRepository.save(clubBook.get());
          }

          return String.format("Book: %s successfully removed from club: %s", removeClubBookRequest.getBookId(), removeClubBookRequest.getNullableCode());
     }

    @Override
     public BookModel upvoteBook(Long bookId){
         return bookAction(bookId, Vote.UP);
     }

    @Override
    public BookModel downvoteBook(Long bookId){
        return bookAction(bookId, Vote.DOWN);
     }

     private BookModel bookAction(Long bookId, Vote vote){

         Book book = checkIfBookExists(bookId);
         User user = userDetailsService.getLoggedInUser();

         Optional <BookVote> bookVote = bookVoteRepository.findByUserAndBook(user, book);
         if (bookVote.isPresent()){
              Vote voteStatus = bookVote.get().getVote();
              if (voteStatus != vote){
                  bookVote.get().setVote(vote);
                  bookVoteRepository.save(bookVote.get());
              }
         } else {
              BookVote newBookVote = BookVote.builder()
                      .user(user)
                      .book(book)
                      .vote(vote)
                      .build();
              bookVoteRepository.save(newBookVote);
         }

         BookWithUpvoteCount bookWithUpvoteCount = bookRepository.findWithVoteCounts(book);

         Long upvoteCount = bookWithUpvoteCount.getUpvoteCount();
         Long downvoteCount = bookWithUpvoteCount.getDownvoteCount();

          return bookMapper.toResponse(book, upvoteCount, downvoteCount);

     }

     public Page<BookModel> getBooks(Pageable pageable){
         return bookRepository.findAllWithVoteCounts(pageable)
                 .map(result -> {
                     Book book = result.getBook();
                     Long upvoteCount = result.getUpvoteCount();
                     Long downvoteCount = result.getDownvoteCount();
                     return bookMapper.toResponse(book, upvoteCount, downvoteCount);
                 });

     }

    @Override
     public BookModel recommendBook(CreateBookRequest recommendBookRequest, String clubCode){

         Club club = clubRepository.findByCode(clubCode)
                 .orElseThrow(() -> new NotFoundException("Club with this code not found"));

         User user = userDetailsService.getLoggedInUser();

         if (!userClubRepository.existsByUserAndClub(user, club)){
             throw new BadRequestException("User is not a member of this club");
         }

         BookRequest bookRequest = BookRequest.builder()
                 .club(club)
                 .user(user)
                 .title(recommendBookRequest.getNullableTitle())
                 .author(recommendBookRequest.getNullableAuthor())
                 .link(recommendBookRequest.getNullableLink())
                 .imageUrl(recommendBookRequest.getNullableImageUrl())
                 .description(recommendBookRequest.getNullableDescription())
                 .bookApprovalStatus(BookApprovalStatus.PENDING)
                 .build();

         bookRequestRepository.save(bookRequest);

         return bookMapper.toDto(bookRequest);

     }

     private BookRequest getBookRequest(Long recommendBookId){
         return bookRequestRepository.findById(recommendBookId)
                 .orElseThrow(() -> new NotFoundException("User Recommended Book with this id not found"));
     }

    @Override
     public String approveRecommendedBook(Long bookId){

         BookRequest bookRequest = getBookRequest(bookId);

         if (bookRequest.getBookApprovalStatus().equals(BookApprovalStatus.PENDING)) {

             CreateBookRequest createBookRequest = new CreateBookRequest();
             createBookRequest.setTitle(bookRequest.getTitle());
             createBookRequest.setLink(bookRequest.getLink());
             createBookRequest.setAuthor(bookRequest.getAuthor());
             createBookRequest.setImageUrl(bookRequest.getImageUrl());
             createBookRequest.setDescription(bookRequest.getDescription());

             Book book = createBook(createBookRequest);
             createClubBook(book.getId(), bookRequest.getClub().getCode());

             bookRequest.setBookApprovalStatus(BookApprovalStatus.APPROVED);
             bookRequestRepository.save(bookRequest);
         }

        return String.format("The recommended book: %s has been approved for Club %s.",
                bookRequest.getTitle(), bookRequest.getClub().getName());

         //send email to user
     }

     @Override
    public String rejectRecommendedBook(Long bookId){

        BookRequest bookRequest = getBookRequest(bookId);

         if (bookRequest.getBookApprovalStatus().equals(BookApprovalStatus.PENDING)) {
             bookRequest.setBookApprovalStatus(BookApprovalStatus.REJECTED);
             bookRequestRepository.save(bookRequest);
         }

        return String.format("The recommended book: %s has been rejected for Club %s.",
                bookRequest.getTitle(), bookRequest.getClub().getName());

        //send email to user
    }

    public Page<BookModel> getAllUpcomingBooks(String code, Pageable pageable) {

        Club club = clubRepository.findByCode(code).orElseThrow(() -> new NotFoundException("Club with this code not found"));

        return clubBookRepository.findNextClubBook(club, PageRequest.of(0, 5))
                .map(clubBook -> {
                    Book book = clubBook.getBook();
                    return bookMapper.toResponse(book, null, null);
                });
    }

    private Book getWeeklyRecommendedBook(Club club){

        ClubBook clubBook =  clubBookRepository.findNextClubBook(club, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElse(null);

        if (clubBook != null) {
            clubBook.setReadDate(LocalDateTime.now());
            clubBookRepository.save(clubBook);
            return clubBook.getBook();
        }
        return null;
    }

    @Override
    public void sendBookRecommendationEmail(){
        DayOfTheWeek today = DayOfTheWeek.valueOf(LocalDate.now().getDayOfWeek().name().toUpperCase());
        List<UserClub> userClubs = userClubRepository.findByClubReadingDay(today);

        for (UserClub userClub : userClubs) {
            User user = userClub.getUser();
            Club club = userClub.getClub();
            Book book = getWeeklyRecommendedBook(club);

            if (book != null) {
                Map<String, Object> params = new HashMap<>();
                params.put("bookTitle", book.getTitle());
                params.put("bookAuthor", book.getAuthor());
                params.put("bookLink", book.getLink());
                params.put("bookImageUrl", book.getImageUrl());
                params.put("bookDescription", book.getDescription());
                params.put("clubName", club.getName());
                params.put("firstname", user.getFirstname());
                emailService.sendMail(user.getEmail(), "Your Weekly Book Recommendation is Here", "weekly-book", params);
            }
        }
    }

}

