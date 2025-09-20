package com.lilybookclub.service;

import com.lilybookclub.dto.request.club.ClubActionByAdminRequest;
import com.lilybookclub.dto.request.club.ClubActionByUserRequest;
import com.lilybookclub.dto.request.club.CreateClubRequest;
import com.lilybookclub.dto.response.club.ClubModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClubService {
    ClubModel createClub(CreateClubRequest createClubRequest);
    Page<ClubModel> getClubs(Pageable pageable);
    ClubModel getClubByCategory(String category);
    ClubModel joinClubByUser(ClubActionByUserRequest clubActionByUserRequest);
    ClubModel joinClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest);
    String leaveClubByUser(ClubActionByUserRequest clubActionByUserRequest);
    String leaveClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest);
}
