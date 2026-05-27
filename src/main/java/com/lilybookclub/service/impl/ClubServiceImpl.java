package com.lilybookclub.service.impl;

import com.lilybookclub.dto.request.club.ClubInterestRequest;
import com.lilybookclub.dto.request.club.CreateClubRequest;
import com.lilybookclub.dto.request.club.ClubActionByAdminRequest;
import com.lilybookclub.dto.response.club.ClubModel;
import com.lilybookclub.dto.response.club.ClubWithMemberCount;
import com.lilybookclub.entity.Club;
import com.lilybookclub.entity.ClubRequest;
import com.lilybookclub.entity.User;
import com.lilybookclub.entity.UserClub;
import com.lilybookclub.enums.Category;
import com.lilybookclub.enums.ClubRole;
import com.lilybookclub.enums.DayOfTheWeek;
import com.lilybookclub.enums.Status;
import com.lilybookclub.exception.BadRequestException;
import com.lilybookclub.exception.NotFoundException;
import com.lilybookclub.mapper.ClubMapper;
import com.lilybookclub.repository.ClubRepository;
import com.lilybookclub.repository.ClubRequestRepository;
import com.lilybookclub.repository.UserClubRepository;
import com.lilybookclub.repository.UserRepository;
import com.lilybookclub.security.UserDetailsServiceImpl;
import com.lilybookclub.service.ClubService;
import com.lilybookclub.service.EmailService;
import com.lilybookclub.service.GeminiService;
import com.lilybookclub.util.AppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;
    private final UserRepository userRepository;
    private final ClubRequestRepository clubRequestRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final EmailService emailService;
    private final GeminiService geminiService;
    private final ClubMapper clubMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private Club createClub(CreateClubRequest createClubRequest){
        if (clubRepository.existsByCode(createClubRequest.getNullableCode())){
            throw new BadRequestException("Club with this code already exists");
        }

        DayOfTheWeek dayOfTheWeek = AppUtil.generateReadingDay();

        Club club = Club.builder()
                .name(createClubRequest.getNullableName())
                .readingDay(dayOfTheWeek)
                .code(createClubRequest.getNullableCode())
                .category(createClubRequest.getCategory())
                .description(createClubRequest.getNullableDescription())
                .build();

        clubRepository.save(club);
        return club;
    }

    @Override
    public ClubModel createClubByAdmin(CreateClubRequest createClubRequest){
         Club club = createClub(createClubRequest);
         return clubMapper.toResponse(club, null);
    }

    // check if code exist fire kafka to check gemini api to verify
    // create add user to club as admin then send email to club admin

    @Override
    public ClubModel requestClub(CreateClubRequest createClubRequest){
        if (clubRepository.existsByCode(createClubRequest.getNullableCode())){
            throw new BadRequestException("Club with this code already exists");
        }

        User user = userDetailsService.getLoggedInUser();

        ClubRequest clubRequest = ClubRequest.builder()
                .user(user)
                .name(createClubRequest.getNullableName())
                .code(createClubRequest.getNullableCode())
                .category(createClubRequest.getCategory())
                .description(createClubRequest.getNullableDescription())
                .clubApprovalStatus(Status.PENDING)
                .build();

        clubRequestRepository.save(clubRequest);

        kafkaTemplate.send("club-request-events", clubRequest.getCode(), clubRequest.getName());

        return clubMapper.toDto(clubRequest);
    }

    @KafkaListener(topics = "club-request-events", groupId = "club-group")
    public void approveClubRequest(
            @Header(KafkaHeaders.RECEIVED_KEY) String clubCode,
            @Payload String clubName){

        ClubRequest clubRequest =  clubRequestRepository.findByCode(clubCode)
                .orElseThrow(() -> new NotFoundException("Club Request with this code not found"));

        if (clubRequest.getClubApprovalStatus().equals(Status.PENDING)) {
            String clubRequestApprovalResult = geminiService.verifyClubRequest(clubRequest.getDescription());

            String cleanResult = clubRequestApprovalResult != null ? clubRequestApprovalResult.trim() : "";
            cleanResult = cleanResult.replace("**", "").replace("\"", "").trim();

            boolean clubRequestIsApproved = Boolean.parseBoolean(cleanResult);

            if (clubRequestIsApproved) {
                //create club
                CreateClubRequest createClubRequest = CreateClubRequest.builder()
                        .name(clubRequest.getName())
                        .code(clubRequest.getCode())
                        .category(clubRequest.getCategory())
                        .description(clubRequest.getDescription())
                        .build();

                Club club = createClub(createClubRequest);

                clubRequest.setClubApprovalStatus(Status.APPROVED);
                clubRequestRepository.save(clubRequest);

                User user = clubRequest.getUser();

                //add user to club as admin
                ClubActionByAdminRequest clubActionByAdminRequest = ClubActionByAdminRequest.builder()
                        .userId(user.getId())
                        .clubCode(clubRequest.getCode())
                        .build();
                joinClubByAdmin(clubActionByAdminRequest);

                //send email
                String mailSubject = String.format("Your Request to create club %s has been approved", club.getName().toLowerCase());

                Map<String, Object> params = new HashMap<>();
                params.put("firstname", user.getFirstname());
                params.put("clubName", club.getName());
                params.put("readingDay", club.getReadingDay().name().toLowerCase());
                params.put("clubCategory", club.getCategory().name().toLowerCase());
                params.put("clubCode", club.getCode());
                params.put("clubDescription", club.getDescription());
                emailService.sendMail(user.getEmail(), mailSubject, "club-request-approved", params);
            }
        }
    }

    @Override
    public Page<ClubModel> getClubs(List<Category> categories, Pageable pageable){
        return clubRepository.findAllWithMemberCount(categories, pageable)
                   .map( result -> {
                            Club club = result.getClub();
                            Long memberCount = result.getMemberCount();
                            return clubMapper.toResponse(club, memberCount);
                           }
                   );
    }

    @Override
    public Page<ClubModel> getClubsByInterest(ClubInterestRequest clubInterestRequest, Pageable pageable){
            String categoryResult = geminiService.getSuggestedCategories(clubInterestRequest.getNullableInterest());

            List<Category> categories = Arrays.stream(categoryResult.split(" "))
                .map(String::trim)
                .map(Category::valueOf)
                .toList();

           return getClubs(categories, pageable);
    }

    @Override
    public ClubModel getClubByCode(String code){
        ClubWithMemberCount clubWithMemberCount = getClubWithMemberCount(code);
        Club club = clubWithMemberCount.getClub();
        Long memberCount = clubWithMemberCount.getMemberCount();

        return clubMapper.toResponse(club, memberCount);
    }

    @Override
    public ClubModel joinClubByUser(String code){
        User user = userDetailsService.getLoggedInUser();
        return createUserClub(code, user, ClubRole.MEMBER);
    }

    @Override
    public ClubModel joinClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest){
        User user = userRepository.findById(clubActionByAdminRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User with this id not found"));
        return createUserClub(clubActionByAdminRequest.getNullableCode(), user, ClubRole.ADMIN);
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

    private ClubModel createUserClub(String code, User user, ClubRole clubRole){
        ClubWithMemberCount clubWithMemberCount = getClubWithMemberCount(code);
        Club club = clubWithMemberCount.getClub();
        Long memberCount = clubWithMemberCount.getMemberCount();

        if (userClubRepository.existsByUserAndClub(user, club)){
              throw new BadRequestException("User is already a member of this club");
        }

        UserClub userClub = UserClub.builder()
                  .club(club)
                  .user(user)
                  .clubRole(clubRole)
                  .build();
          userClubRepository.save(userClub);

        String mailSubject = String.format("You’re now a member of %s club", club.getName().toLowerCase());

        Map<String, Object> params = new HashMap<>();
        params.put("firstname", user.getFirstname());
        params.put("clubName", club.getName());
        params.put("readingDay", club.getReadingDay().name().toLowerCase());
        params.put("clubCategory", club.getCategory().name().toLowerCase());
        params.put("clubCode", club.getCode());
        params.put("clubDescription", club.getDescription());
        params.put("totalMembers", memberCount+1);
        emailService.sendMail(user.getEmail(), mailSubject, "club-welcome", params);

        return clubMapper.toResponse(club, memberCount);
    }

    private String deleteUserClub(String code, User user){

        Club club = checkIfClubExists(code);
        Optional <UserClub> userClub = userClubRepository.findByUserAndClub(user, club);

        userClub.ifPresent(userClubRepository::delete);

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
