package com.lilybookclub.repository;

import com.lilybookclub.entity.Club;
import com.lilybookclub.entity.ClubRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRequestRepository extends JpaRepository<ClubRequest,Long> {
    Optional<ClubRequest> findByCode(String code);
}
