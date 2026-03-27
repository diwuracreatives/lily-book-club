package com.lilybookclub.repository;

import com.lilybookclub.entity.RecommendedBook;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecommendedBookRepository  extends JpaRepository<RecommendedBook, Long> {
}
