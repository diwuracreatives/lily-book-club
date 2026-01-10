package com.lilybookclub.repository;

import com.lilybookclub.entity.BookRequest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
}
