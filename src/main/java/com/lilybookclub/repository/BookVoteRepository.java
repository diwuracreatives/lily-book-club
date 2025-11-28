package com.lilybookclub.repository;

import com.lilybookclub.entity.Book;
import com.lilybookclub.entity.BookVote;
import com.lilybookclub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookVoteRepository extends JpaRepository<BookVote, Long> {
    Optional<BookVote> findByUserAndBook(User user, Book book);
}
