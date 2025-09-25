package com.lilybookclub.service;

import com.lilybookclub.dto.request.club.ClubActionByAdminRequest;
import com.lilybookclub.dto.request.club.CreateClubRequest;
import com.lilybookclub.dto.response.club.ClubModel;
import com.lilybookclub.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubService {
    ClubModel createClub(CreateClubRequest createClubRequest);
    Page<ClubModel> getClubs(Category category, Pageable pageable);
    ClubModel getClubByCode(String code);
    ClubModel joinClubByUser(String category);
    ClubModel joinClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest);
    String leaveClubByUser(String category);
    String leaveClubByAdmin(ClubActionByAdminRequest clubActionByAdminRequest);
}
