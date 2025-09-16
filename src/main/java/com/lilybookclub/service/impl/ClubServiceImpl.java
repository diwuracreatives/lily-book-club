package com.lilybookclub.service.impl;

import com.lilybookclub.dto.request.club.CreateClubRequest;
import com.lilybookclub.dto.request.club.ClubActionByAdminRequest;
import com.lilybookclub.dto.request.club.ClubActionByUserRequest;
import com.lilybookclub.dto.response.user.ClubModel;
import com.lilybookclub.entity.Club;
import com.lilybookclub.entity.ClubCategory;
import com.lilybookclub.entity.User;
import com.lilybookclub.entity.UserClub;
import com.lilybookclub.exception.AlreadyExistException;
import com.lilybookclub.exception.NotFoundException;
import com.lilybookclub.repository.ClubCategoryRepository;
import com.lilybookclub.repository.ClubRepository;
import com.lilybookclub.repository.UserClubRepository;
import com.lilybookclub.repository.UserRepository;
import com.lilybookclub.security.UserDetailsServiceImpl;
import com.lilybookclub.service.ClubService;
import com.lilybookclub.util.ClubUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final ClubCategoryRepository clubCategoryRepository;
    private final UserClubRepository userClubRepository;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public ClubModel CreateClub(CreateClubRequest createClubRequest){
        if (clubCategoryRepository.existsByName(createClubRequest.getCategory().toLowerCase())) {
            throw new AlreadyExistException("Club with this category already exist!");
        }

        ClubCategory clubCategory = ClubCategory.builder()
                .name(createClubRequest.getCategory())
                .build();
        clubCategoryRepository.save(clubCategory);

        if (clubRepository.existsByName(createClubRequest.getName().toLowerCase())){
               throw new AlreadyExistException("Club with this name already exists");
        }

        String clubCode = ClubUtil.generateClubCode();
        DayOfWeek readingDay = ClubUtil.generateReadingDay();

        Club club = Club.builder()
                .code(clubCode)
                .name(createClubRequest.getName())
                .readingDay(readingDay.getValue())
                .category(clubCategory)
                .build();
        clubRepository.save(club);

        return ClubModel.builder()
                .code(clubCode)
                .name(createClubRequest.getName())
                .category(createClubRequest.getCategory().toLowerCase())
                .readingDay(readingDay.name())
                .build();
    }

    @Override
    public List<ClubModel> GetClubs(){
           return clubRepository.findAll()
                   .stream()
                   .map(club -> ClubModel.builder()
                           .code(club.getCode())
                           .name(club.getName())
                           .category(club.getCategory().getName())
                           .readingDay(ClubUtil.getReadingDay(club.getReadingDay()))
                           .membersCount(userClubRepository.countByClubId(club.getId()))
                           .build())
                   .toList();
    }

    @Override
    public ClubModel GetClubByCategory(String category){
          ClubCategory clubCategory =  clubCategoryRepository.findByName(category.toLowerCase())
                .orElseThrow(() -> new NotFoundException("Category with this name not found"));

          Club club =  clubRepository.findByCategoryId(clubCategory.getId());

        return ClubModel.builder()
                .code(club.getCode())
                .name(club.getName())
                .category(clubCategory.getName())
                .readingDay(ClubUtil.getReadingDay(club.getReadingDay()))
                .membersCount(userClubRepository.countByClubId(club.getId()))
                .build();
    }

    @Override
    public ClubModel JoinClubByUser(ClubActionByUserRequest clubActionByUserRequest){
        User user = userDetailsService.getLoggedInUser();
        return createUserClub(clubActionByUserRequest.getClubCode(), user.getId());
    }

    @Override
    public ClubModel JoinClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest){
        return createUserClub(clubActionByAdminRequest.getClubCode(), clubActionByAdminRequest.getUserId());
    }

    @Transactional
    @Override
    public String LeaveClubByUser(ClubActionByUserRequest clubActionByUserRequest){
        User user = userDetailsService.getLoggedInUser();
        return deleteUserClub(clubActionByUserRequest.getClubCode(), user.getId());
    }

    @Transactional
    @Override
    public String LeaveClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest){
        return deleteUserClub(clubActionByAdminRequest.getClubCode(), clubActionByAdminRequest.getUserId());
    }

    private ClubModel createUserClub(String clubCode, long userId){
          Club club = clubRepository.findByCode(clubCode)
                  .orElseThrow(() -> new NotFoundException("Club with this code not found"));
          User user = userRepository.findById(userId)
                  .orElseThrow(() -> new NotFoundException("User with this id not found"));

          if (userClubRepository.existsByUserAndClub(user, club)){
              throw new AlreadyExistException("User is already a member of this club");
          }

        UserClub userClub = UserClub.builder()
                  .club(club)
                  .user(user)
                  .build();
          userClubRepository.save(userClub);

          return ClubModel.builder()
                .code(club.getCode())
                .name(club.getName())
                .category(club.getCategory().getName())
                .readingDay(ClubUtil.getReadingDay(club.getReadingDay()))
                .membersCount(userClubRepository.countByClubId(club.getId()))
                .build();
    }

    private String deleteUserClub(String clubCode, long userId){
        Club club = clubRepository.findByCode(clubCode)
                .orElseThrow(() -> new NotFoundException("Club with this code not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with this id not found"));
        String name = club.getName();
        UserClub userClub = userClubRepository.findByUserAndClub(user, club)
                .orElseThrow(() -> new NotFoundException("User is not a member of this club"));

        userClubRepository.deleteByUserAndClub(user, club);
        return String.format(name, "%s User successfully removed from club:");
    }

}
