package com.lilybookclub.repository;

import com.lilybookclub.dto.response.book.BookWithUpvoteCount;
import com.lilybookclub.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book,Long> {

    @Query("""
        SELECT b AS book, COUNT(CASE WHEN bv.vote = 'UP' THEN 1 END) AS upvoteCount,
        COUNT(CASE WHEN bv.vote = 'DOWN' THEN 1 END) AS downvoteCount
        FROM Book b
        LEFT JOIN BookVote bv ON bv.book = b
        GROUP BY b
        """)
    Page<BookWithUpvoteCount> findAllWithVoteCounts(Pageable pageable);

    @Query("""
        SELECT b AS book, COUNT(CASE WHEN bv.vote = 'UP' THEN 1 END) AS upvoteCount,
        COUNT(CASE WHEN bv.vote = 'DOWN' THEN 1 END) AS downvoteCount
        FROM Book b
        LEFT JOIN BookVote bv ON bv.book = b
        WHERE b = :book
        GROUP BY b
        """)
    BookWithUpvoteCount findWithVoteCounts(@Param("book") Book book);



}

