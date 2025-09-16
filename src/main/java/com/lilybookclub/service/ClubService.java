package com.lilybookclub.service;

import com.lilybookclub.dto.request.club.ClubActionByAdminRequest;
import com.lilybookclub.dto.request.club.ClubActionByUserRequest;
import com.lilybookclub.dto.request.club.CreateClubRequest;
import com.lilybookclub.dto.response.user.ClubModel;

import java.util.List;

public interface ClubService {
    ClubModel CreateClub(CreateClubRequest createClubRequest);
    List<ClubModel> GetClubs();
    ClubModel GetClubByCategory(String category);
    ClubModel JoinClubByUser(ClubActionByUserRequest clubActionByUserRequest);
    ClubModel JoinClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest);
    String LeaveClubByUser(ClubActionByUserRequest clubActionByUserRequest);
    String LeaveClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest);
}
