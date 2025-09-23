package com.lilybookclub.service.impl;

import com.lilybookclub.dto.request.club.CreateClubRequest;
import com.lilybookclub.dto.request.club.ClubActionByAdminRequest;
import com.lilybookclub.dto.request.club.ClubActionByUserRequest;
import com.lilybookclub.dto.response.club.ClubModel;
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
import com.lilybookclub.util.ClubUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    private Category getCategory(String category){
        return Category.valueOf(category);
    }

    @Override
    public ClubModel createClub(CreateClubRequest createClubRequest){

        if (clubRepository.existsByCode(createClubRequest.getNullableCode())) {
            throw new BadRequestException(String.format("Club with code %s already exists", createClubRequest.getNullableCode()));
        }

        DayOfTheWeek dayOfTheWeek = ClubUtil.generateReadingDay();

        Club club = Club.builder()
                .name(createClubRequest.getNullableName())
                .readingDay(dayOfTheWeek)
                .category(createClubRequest.getCategory())
                .code(createClubRequest.getNullableCode())
                .description(createClubRequest.getNullableDescription())
                .build();
        clubRepository.save(club);

        return ClubModel.builder()
                .name(createClubRequest.getName().trim().toLowerCase())
                .category(createClubRequest.getCategory())
                .readingDay(dayOfTheWeek.name())
                .description(createClubRequest.getDescription())
                .build();
    }

    @Override
    public Page<ClubModel> getClubs(Pageable pageable) {
        return clubRepository.findAllWithMemberCount(pageable)
                .map(res -> {
                            Club club = (Club) res[0];
                            Long memberCount = (Long) res[1];

                            return ClubModel.builder()
                                    .name(club.getName())
                                    .category(club.getCategory())
                                    .readingDay(club.getReadingDay().name())
                                    .description(club.getDescription())
                                    .membersCount(memberCount)
                                    .build();
                        }
                );
    }


    @Override
    public ClubModel getClubByCategory(String category){

        Object[] res = clubRepository.findWithMemberCountByCategory(getCategory(category));

        if (res == null) {
            throw new NotFoundException("Club with this category not found");
        }

        Club club = (Club) res[0];
        Long memberCount = (Long) res[1];
        return ClubModel.builder()
                .name(club.getName())
                .category(club.getCategory())
                .readingDay(club.getReadingDay().name())
                .description(club.getDescription())
                .membersCount(memberCount)
                .build();
    }

    @Override
    public ClubModel joinClubByUser(ClubActionByUserRequest clubActionByUserRequest){
        User user = userDetailsService.getLoggedInUser();
        return createUserClub(clubActionByUserRequest.getCategory(), user);
    }

    @Override
    public ClubModel joinClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest){
        User user = userRepository.findById(clubActionByAdminRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User with this id not found"));
        return createUserClub(clubActionByAdminRequest.getCategory(), user);
    }

    @Transactional
    @Override
    public String leaveClubByUser(ClubActionByUserRequest clubActionByUserRequest){
        User user = userDetailsService.getLoggedInUser();
        return deleteUserClub(clubActionByUserRequest.getCategory(), user);
    }

    @Transactional
    @Override
    public String leaveClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest){
        User user = userRepository.findById(clubActionByAdminRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User with this id not found"));
        return deleteUserClub(clubActionByAdminRequest.getCategory(), user);
    }

    private ClubModel createUserClub(String category, User user){
        Object[] res = clubRepository.findWithMemberCountByCategory(getCategory(category));

        if (res == null) {
            throw new NotFoundException("Club with this category not found");
        }

        Club club = (Club) res[0];
        Long memberCount = (Long) res[1];

        if (userClubRepository.existsByUserAndClub(user, club)){
              throw new BadRequestException("User is already a member of this club");
        }

        UserClub userClub = UserClub.builder()
                  .club(club)
                  .user(user)
                  .build();
          userClubRepository.save(userClub);

        return ClubModel.builder()
                .name(club.getName())
                .category(club.getCategory())
                .readingDay(club.getReadingDay().name())
                .description(club.getDescription())
                .membersCount(memberCount)
                .build();
    }

    private String deleteUserClub(String category, User user){
        Club club = checkIfClubExists(category);
        Optional <UserClub> userClub = userClubRepository.findByUserAndClub(user, club);

         if (userClub.isPresent()){
             userClubRepository.deleteByUserAndClub(user, club);
         }

        String name = club.getName();
        return String.format("User successfully removed from club: %s", name);
    }

    private Club checkIfClubExists(String category){
        return clubRepository.findByCategory(getCategory(category))
                .orElseThrow(() -> new NotFoundException("Club with this category not found"));
    }

}
