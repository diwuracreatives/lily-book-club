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

    @Override
    public ClubModel createClub(CreateClubRequest createClubRequest){

        if (clubRepository.existsByCode(createClubRequest.getNullableCode())){
            throw new BadRequestException("Club with this code already exists");
        }

        DayOfTheWeek dayOfTheWeek = ClubUtil.generateReadingDay();

        Club club = Club.builder()
                .name(createClubRequest.getNullableName())
                .readingDay(dayOfTheWeek)
                .code(createClubRequest.getNullableCode())
                .category(createClubRequest.getCategory())
                .description(createClubRequest.getNullableDescription())
                .build();
        clubRepository.save(club);

        return ClubModel.builder()
                .name(createClubRequest.getNullableName())
                .code(createClubRequest.getNullableCode())
                .category(createClubRequest.getCategory())
                .readingDay(dayOfTheWeek.name())
                .description(createClubRequest.getNullableDescription())
                .build();
    }

    @Override
    public Page<ClubModel> getClubs(Category category, Pageable pageable){
        return clubRepository.findAllWithMemberCount(category, pageable)
                   .map( result -> {
                            Club club = result.getClub();
                            Long memberCount = result.getMemberCount();
                            return  ClubModel.builder()
                           .name(club.getName())
                            .code(club.getCode())
                           .category(club.getCategory())
                           .readingDay(club.getReadingDay().name())
                           .description(club.getDescription())
                           .membersCount(memberCount)
                           .build();
                   }
                   );
    }

    @Override
    public ClubModel getClubByCode(String code){

        ClubWithMemberCount clubWithMemberCount = getClubWithMemberCount(code);
        Club club = clubWithMemberCount.getClub();
        Long memberCount = clubWithMemberCount.getMemberCount();

        return ClubModel.builder()
                .name(club.getName())
                .code(club.getCode())
                .category(club.getCategory())
                .readingDay(club.getReadingDay().name())
                .description(club.getDescription())
                .membersCount(memberCount)
                .build();
    }

    @Override
    public ClubModel joinClubByUser(String code){
        User user = userDetailsService.getLoggedInUser();
        return createUserClub(code, user);
    }

    @Override
    public ClubModel joinClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest){
        User user = userRepository.findById(clubActionByAdminRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User with this id not found"));
        return createUserClub(clubActionByAdminRequest.getNullableCode(), user);
    }

    @Override
    public String leaveClubByUser(String code){
        User user = userDetailsService.getLoggedInUser();
        return deleteUserClub(code, user);
    }

    @Override
    public String leaveClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest){
        User user = userRepository.findById(clubActionByAdminRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User with this id not found"));
        return deleteUserClub(clubActionByAdminRequest.getNullableCode(), user);
    }

    private ClubModel createUserClub(String code, User user){

        ClubWithMemberCount clubWithMemberCount = getClubWithMemberCount(code);
        Club club = clubWithMemberCount.getClub();
        Long memberCount = clubWithMemberCount.getMemberCount();

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
                .category(club.getCategory())
                .code(club.getCode())
                .readingDay(club.getReadingDay().name())
                .description(club.getDescription())
                .membersCount(memberCount)
                .build();
    }

    private String deleteUserClub(String code, User user){

        Club club = checkIfClubExists(code);
        Optional <UserClub> userClub = userClubRepository.findByUserAndClub(user, club);

        if (userClub.isPresent()) {
            userClub.get().setIsDeleted(true);
            userClubRepository.save(userClub.get());
        }

        String name = club.getName();
        return String.format("User successfully removed from club: %s", name);

    }

    private Club checkIfClubExists(String code){
        return clubRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Club with this code not found"));
    }

    private ClubWithMemberCount getClubWithMemberCount(String code){
        return clubRepository.findWithMemberCountByCode(code)
                .orElseThrow(() -> new NotFoundException("Club with this code not found"));
    }

}
