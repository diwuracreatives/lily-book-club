package com.lilybookclub.repository;

import com.lilybookclub.entity.Club;
import com.lilybookclub.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClubRepository extends JpaRepositoryImplementation<Club,Long> {
      boolean existsByCategory(Category category);
      Optional <Club> findByCategory(Category category);
      @Query("""
        SELECT c, COUNT(uc)
        FROM Club c
        LEFT JOIN UserClub uc ON uc.club = c
        GROUP BY c
      """)
      Page<Object[]> findAllWithMemberCount(Pageable pageable);

      @Query("""
        SELECT c, COUNT(uc)
        FROM Club c
        LEFT JOIN UserClub uc ON uc.club = c
        WHERE c.category = :category
        GROUP BY c
      """)
      Object[] findWithMemberCountByCategory(@Param("category") Category category);

      boolean existsByCode(String code);
}

