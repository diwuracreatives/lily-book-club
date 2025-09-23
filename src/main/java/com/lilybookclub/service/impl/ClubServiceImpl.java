package com.lilybookclub.service.impl;

import com.lilybookclub.dto.request.club.CreateClubRequest;
import com.lilybookclub.dto.request.club.ClubActionByAdminRequest;
import com.lilybookclub.dto.response.club.ClubModel;
import com.lilybookclub.dto.response.club.ClubWithMemberCount;
import com.lilybookclub.entity.Club;
import com.lilybookclub.entity.User;
import com.lilybookclub.entity.UserClub;
import com.lilybookclub.enums.Category;
import com.lilybookclub.enums.DayOfTheWeek;
import com.lilybookclub.exception.BadRequestException;
import com.lilybookclub.exception.NotFoundException;
import com.lilybookclub.repository.ClubRepository;
import com.lilybookclub.repository.UserClubRepository;
import com.lilybookclub.repository.UserRepository;
import com.lilybookclub.security.UserDetailsServiceImpl;
import com.lilybookclub.service.ClubService;
import com.lilybookclub.service.EmailService;
import com.lilybookclub.util.ClubUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final EmailService emailService;

    private Category getCategory(String category){
        try {
            return Category.valueOf(category.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new NotFoundException("Category Not Found");
        }

    }

    @Override
    public ClubModel createClub(CreateClubRequest createClubRequest){

        Category category = getCategory(createClubRequest.getCategory());
        if (clubRepository.existsByCategory(category)) {
            throw new BadRequestException("Club with this category already exist!");
        }

        DayOfTheWeek dayOfTheWeek = ClubUtil.generateReadingDay();

        Club club = Club.builder()
                .name(createClubRequest.getName().trim().toLowerCase())
                .readingDay(dayOfTheWeek)
                .category(category)
                .description(createClubRequest.getDescription().trim().toLowerCase())
                .build();
        clubRepository.save(club);

        return ClubModel.builder()
                .name(createClubRequest.getName().trim().toLowerCase())
                .category(createClubRequest.getCategory())
                .readingDay(dayOfTheWeek.name())
                .description(createClubRequest.getDescription().trim())
                .build();
    }

    @Override
    public Page<ClubModel> getClubs(Pageable pageable){
        return clubRepository.findAllWithMemberCount(pageable)
                   .map( result -> {
                            Club club = result.getClub();
                            Long memberCount = result.getMemberCount();
                            return  ClubModel.builder()
                           .name(club.getName())
                           .category(club.getCategory().name())
                           .readingDay(club.getReadingDay().name())
                           .description(club.getDescription())
                           .membersCount(memberCount)
                           .build();
                   }
                   );
    }

    @Override
    public ClubModel getClubByCategory(String category){
        ClubWithMemberCount result = clubRepository.findWithMemberCountByCategory(getCategory(category))
                .orElseThrow(() -> new NotFoundException("Club with this category not found"));

        Club club = result.getClub();
        Long memberCount = result.getMemberCount();

        return ClubModel.builder()
                .name(club.getName())
                .category(club.getCategory().name())
                .readingDay(club.getReadingDay().name())
                .description(club.getDescription())
                .membersCount(memberCount)
                .build();
    }

    @Override
    public ClubModel joinClubByUser(String category){
        User user = userDetailsService.getLoggedInUser();
        return createUserClub(category, user);
    }

    @Override
    public ClubModel joinClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest){
        User user = userRepository.findById(clubActionByAdminRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User with this id not found"));
        return createUserClub(clubActionByAdminRequest.getCategory(), user);
    }

    @Override
    public String leaveClubByUser(String category){
        User user = userDetailsService.getLoggedInUser();
        return deleteUserClub(category, user);
    }

    @Override
    public String leaveClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest){
        User user = userRepository.findById(clubActionByAdminRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User with this id not found"));
        return deleteUserClub(clubActionByAdminRequest.getCategory(), user);
    }

    private ClubModel createUserClub(String category, User user){
        ClubWithMemberCount result = clubRepository.findWithMemberCountByCategory(getCategory(category))
                .orElseThrow(() -> new NotFoundException("Club with this category not found"));

        Club club = result.getClub();
        Long memberCount = result.getMemberCount();

        if (userClubRepository.existsByUserAndClub(user, club)){
              throw new BadRequestException("User is already a member of this club");
        }

        UserClub userClub = UserClub.builder()
                  .club(club)
                  .user(user)
                  .build();
          userClubRepository.save(userClub);

        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getFirstname());
        params.put("clubName", club.getName());
        emailService.sendMail(user.getEmail(), "Welcome to Lily Book Club", "club-welcome", params);

        return ClubModel.builder()
                .name(club.getName())
                .category(club.getCategory().name())
                .readingDay(club.getReadingDay().name())
                .description(club.getDescription())
                .membersCount(memberCount)
                .build();
    }

    private String deleteUserClub(String category, User user){
        Club club = checkIfClubExists(category);
        Optional <UserClub> userClub = userClubRepository.findByUserAndClub(user, club);

        if (userClub.isPresent()) {
            userClub.get().setIsDeleted(true);
            userClubRepository.save(userClub.get());
        }

        String name = club.getName();
        return String.format("User successfully removed from club: %s", name);
    }

    private Club checkIfClubExists(String category){
        return clubRepository.findByCategory(getCategory(category))
                .orElseThrow(() -> new NotFoundException("Club with this category not found"));
    }

}
