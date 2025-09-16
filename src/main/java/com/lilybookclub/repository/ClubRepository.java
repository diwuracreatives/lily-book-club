package com.lilybookclub.repository;

import com.lilybookclub.entity.Club;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;

public interface ClubRepository extends JpaRepositoryImplementation<Club,Long> {
      boolean existsByName(String name);
      Club findByCategoryId(Long categoryId);
      Optional<Club> findByCode(String clubCode);
}
