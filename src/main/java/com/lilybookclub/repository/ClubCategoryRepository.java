package com.lilybookclub.repository;

import com.lilybookclub.entity.ClubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubCategoryRepository extends JpaRepository<ClubCategory, Long> {
    boolean existsByName(String name);
    Optional<ClubCategory> findByName(String name);
}
