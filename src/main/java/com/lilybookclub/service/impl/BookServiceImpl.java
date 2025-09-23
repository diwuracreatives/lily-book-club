package com.lilybookclub.service.impl;

import com.lilybookclub.dto.request.book.ClubBookRequest;
import com.lilybookclub.dto.request.book.CreateBookRequest;
import com.lilybookclub.dto.request.book.RecommendBookRequest;
import com.lilybookclub.dto.response.book.BookModel;
import com.lilybookclub.dto.response.book.BookWithUpvoteCount;
import com.lilybookclub.entity.*;
import com.lilybookclub.enums.BookApprovalStatus;
import com.lilybookclub.enums.DayOfTheWeek;
import com.lilybookclub.enums.Vote;
import com.lilybookclub.exception.BadRequestException;
import com.lilybookclub.exception.NotFoundException;
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
      private final RecommendedBookRepository recommendedBookRepository;
      private final EmailService emailService;



    private Book checkIfBookExists(Long bookId){
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book with this id not found"));
     };

    private Club checkIfClubExists(Long clubId){
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new NotFoundException("Club with this id not found"));
    }


    private Book createBook(CreateBookRequest createBookRequest){
         Book book = Book.builder()
                 .title(createBookRequest.getTitle().trim().toLowerCase())
                 .author(createBookRequest.getAuthor().trim().toLowerCase())
                 .link(createBookRequest.getLink().trim().toLowerCase())
                 .imageUrl(createBookRequest.getImageUrl().trim().toLowerCase())
                 .description(createBookRequest.getDescription().trim())
                 .build();
         bookRepository.save(book);
         return book;
     };

     private String createClubBook(Long bookId, Long clubId){
         ClubBook clubBook = ClubBook.builder()
                 .book(checkIfBookExists(bookId))
                 .club(checkIfClubExists(clubId))
                 .build();
         clubBookRepository.save(clubBook);

         return String.format("Book: %s successfully added to club: %s", bookId, clubId);
     }

    @Override
     public BookModel addBookByAdmin(CreateBookRequest createBookRequest){
           createBook(createBookRequest);
           return BookModel.builder()
                   .title(createBookRequest.getTitle())
                   .author(createBookRequest.getAuthor())
                   .link(createBookRequest.getLink())
                   .imageUrl(createBookRequest.getImageUrl())
                   .description(createBookRequest.getDescription())
                   .build();
     }

    @Override
     public String addBookToClub(ClubBookRequest createClubBookRequest){
         return createClubBook(createClubBookRequest.getBookId(), createClubBookRequest.getClubId());
     }

    @Override
     public String removeBookFromClub(ClubBookRequest removeClubBookRequest){
          Book book = checkIfBookExists(removeClubBookRequest.getBookId());
          Club club = checkIfClubExists(removeClubBookRequest.getClubId());
          Optional<ClubBook> clubBook = clubBookRepository.findByClubAndBook(club, book);
          if (clubBook.isPresent()) {
              clubBook.get().setIsDeleted(true);
              clubBookRepository.save(clubBook.get());
          }
          return String.format("Book: %s successfully removed from club: %s", removeClubBookRequest.getBookId(), removeClubBookRequest.getClubId());
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

         return BookModel.builder()
                 .title(book.getTitle())
                 .author(book.getAuthor())
                 .link(book.getLink())
                 .imageUrl(book.getImageUrl())
                 .description(book.getDescription())
                 .upvoteCount(upvoteCount)
                 .downvoteCount(downvoteCount)
                 .build();
     }

     public Page<BookModel> getBooks(Pageable pageable){
         return bookRepository.findAllWithVoteCounts(pageable)
                 .map(result -> {
                     Book book = result.getBook();
                     Long upvoteCount = result.getUpvoteCount();
                     Long downvoteCount = result.getDownvoteCount();
                     return BookModel.builder()
                             .title(book.getTitle())
                             .author(book.getAuthor())
                             .link(book.getLink())
                             .imageUrl(book.getImageUrl())
                             .description(book.getDescription())
                             .upvoteCount(upvoteCount)
                             .downvoteCount(downvoteCount)
                             .build();
                 });
     }

    @Override
     public BookModel recommendBook(RecommendBookRequest recommendBookRequest){
         Club club = checkIfClubExists(recommendBookRequest.getClubId());
         User user = userDetailsService.getLoggedInUser();

         if (!userClubRepository.existsByUserAndClub(user, club)){
             throw new BadRequestException("User is not a member of this club");
         }

         RecommendedBook recommendedBook = RecommendedBook.builder()
                 .club(club)
                 .user(user)
                 .title(recommendBookRequest.getTitle().trim().toLowerCase())
                 .author(recommendBookRequest.getAuthor().trim().toLowerCase())
                 .link(recommendBookRequest.getLink().trim().toLowerCase())
                 .imageUrl(recommendBookRequest.getImageUrl().trim().toLowerCase())
                 .description(recommendBookRequest.getDescription().trim().toLowerCase())
                 .bookApprovalStatus(BookApprovalStatus.PENDING)
                 .build();

         recommendedBookRepository.save(recommendedBook);

         return BookModel.builder()
                 .title(recommendBookRequest.getTitle())
                 .author(recommendBookRequest.getAuthor())
                 .link(recommendBookRequest.getLink())
                 .imageUrl(recommendBookRequest.getImageUrl())
                 .description(recommendBookRequest.getDescription())
                 .build();
     }

     private RecommendedBook getRecommendedBook(Long recommendBookId){
         return recommendedBookRepository.findById(recommendBookId)
                 .orElseThrow(() -> new NotFoundException("User Recommended Book with this id not found"));
     }

    @Override
     public String approveRecommendedBook(Long bookId){
         RecommendedBook recommendedBook = getRecommendedBook(bookId);

         if (recommendedBook.getBookApprovalStatus().equals(BookApprovalStatus.PENDING)) {
             CreateBookRequest createBookRequest = new CreateBookRequest();
             createBookRequest.setTitle(recommendedBook.getTitle());
             createBookRequest.setLink(recommendedBook.getLink());
             createBookRequest.setAuthor(recommendedBook.getAuthor());
             createBookRequest.setImageUrl(recommendedBook.getImageUrl());
             createBookRequest.setDescription(recommendedBook.getDescription());

             Book book = createBook(createBookRequest);
             createClubBook(book.getId(), recommendedBook.getClub().getId());

             recommendedBook.setBookApprovalStatus(BookApprovalStatus.APPROVED);
             recommendedBookRepository.save(recommendedBook);
         }

        return String.format("The recommended book: %s has been approved for Club %s.",
                recommendedBook.getTitle(), recommendedBook.getClub().getName());

         //send email to user
     }

     @Override
    public String rejectRecommendedBook(Long bookId){
        RecommendedBook recommendedBook = getRecommendedBook(bookId);

         if (recommendedBook.getBookApprovalStatus().equals(BookApprovalStatus.PENDING)) {
             recommendedBook.setBookApprovalStatus(BookApprovalStatus.REJECTED);
             recommendedBookRepository.save(recommendedBook);
         }

        return String.format("The recommended book: %s has been rejected for Club %s.",
                recommendedBook.getTitle(), recommendedBook.getClub().getName());
        //send email to user
    }

    private Page<ClubBook> findNextClubBooks(Club club, Pageable pageable){
         return clubBookRepository.findNextClubBook(club, pageable);
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
                params.put("clubName", club.getName());
                params.put("firstname", user.getFirstname());
                emailService.sendMail(user.getEmail(), "Your Weekly Book Recommendation is Here", "weekly-book", params);
            }
        }
    }

}

