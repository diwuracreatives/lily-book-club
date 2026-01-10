package com.lilybookclub.repository;

import com.lilybookclub.dto.response.club.ClubWithMemberCount;
import com.lilybookclub.entity.Club;
import com.lilybookclub.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club,Long> {

      boolean existsByCode(String code);

      Optional <Club> findByCode(String code);

      @Query("""
        SELECT c AS club, COUNT(uc) AS memberCount
        FROM Club c
        LEFT JOIN UserClub uc ON uc.club = c
        WHERE (:categories IS NULL OR c.category IN (:categories))
        GROUP BY c
        """)
      Page<ClubWithMemberCount> findAllWithMemberCount(@Param("categories") List<Category> categories, Pageable pageable);

      @Query("""
        SELECT c as club, COUNT(uc) as memberCount
        FROM Club c
        LEFT JOIN UserClub uc ON uc.club = c
        WHERE c.code = :code
        GROUP BY c
        """)
      Optional<ClubWithMemberCount> findWithMemberCountByCode(@Param("code") String code);
}

