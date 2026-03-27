package com.lilybookclub.repository;

import com.lilybookclub.entity.Club;
import com.lilybookclub.entity.User;
import com.lilybookclub.entity.UserClub;
import com.lilybookclub.enums.DayOfTheWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserClubRepository extends JpaRepository<UserClub,Long> {
      boolean existsByUserAndClub(User user, Club club);
      Optional<UserClub> findByUserAndClub(User user, Club club);
      void deleteByUserAndClub(User user, Club club);
      List<UserClub> findByClubReadingDay(DayOfTheWeek dayOfTheWeek);
}
