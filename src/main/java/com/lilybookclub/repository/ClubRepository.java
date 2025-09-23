package com.lilybookclub.repository;

import com.lilybookclub.dto.response.club.ClubWithMemberCount;
import com.lilybookclub.entity.Club;
import com.lilybookclub.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club,Long> {
      boolean existsByCategory(Category category);
      Optional <Club> findByCategory(Category category);
      @Query("""
        SELECT c AS club, COUNT(uc) AS memberCount
        FROM Club c
        LEFT JOIN UserClub uc ON uc.club = c
        GROUP BY c
        """)
       Page<ClubWithMemberCount> findAllWithMemberCount(Pageable pageable);

      @Query("""
        SELECT c as club, COUNT(uc) as memberCount
        FROM Club c
        LEFT JOIN UserClub uc ON uc.club = c
        WHERE c.category = :category
        GROUP BY c
        """)
      Optional<ClubWithMemberCount> findWithMemberCountByCategory(@Param("category") Category category);
}

