package com.lilybookclub.repository;

import com.lilybookclub.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClubBookRepository extends JpaRepository<ClubBook, Long> {
    Optional<ClubBook> findByClubAndBook(Club club, Book book);

    @Query("""
           SELECT cb
           FROM ClubBook cb
           LEFT JOIN BookVote bv
               ON bv.book = cb.book
           WHERE cb.club = :club
           GROUP BY cb
           ORDER BY
               CASE WHEN cb.readDate IS NULL THEN 0 ELSE 1 END,
               SUM(CASE WHEN bv.vote = 'UP' THEN 1 ELSE 0 END) DESC,
               cb.createdAt ASC
        """)
    Page<ClubBook> findNextClubBook(@Param("club") Club club, Pageable pageable);

}
