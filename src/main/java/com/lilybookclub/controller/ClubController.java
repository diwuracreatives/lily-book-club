package com.lilybookclub.controller;

import com.lilybookclub.dto.request.club.ClubActionByAdminRequest;
import com.lilybookclub.dto.request.club.ClubActionByUserRequest;
import com.lilybookclub.dto.request.club.CreateClubRequest;
import com.lilybookclub.dto.response.club.ClubModel;
import com.lilybookclub.service.ClubService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/v1/clubs")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Club Management", description = "Endpoints for managing club by user and admin")
public class ClubController {
    private final ClubService clubService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ClubModel createClub(@RequestBody @Valid CreateClubRequest createClubRequest) {
        return clubService.createClub(createClubRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ClubModel> getClubs(@PageableDefault(size = 10, page = 0, sort = "name") Pageable pageable) {
        return clubService.getClubs(pageable);
    }

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public ClubModel getClubByCategory(@RequestParam("category") String category) {
        return clubService.getClubByCategory(category);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public ClubModel joinClubByUser(@RequestBody @Valid ClubActionByUserRequest clubActionByUserRequest) {
        return clubService.joinClubByUser(clubActionByUserRequest);
    }

    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> leaveClubByUser(@RequestBody @Valid ClubActionByUserRequest clubActionByUserRequest) {
        String message = clubService.leaveClubByUser(clubActionByUserRequest);
        return Map.of("message", message);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ClubModel joinClubByAdmin(@RequestBody @Valid ClubActionByAdminRequest clubActionByAdminRequest) {
        return  clubService.joinClubByAdmin(clubActionByAdminRequest);
    }

    @DeleteMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> leaveClubByAdmin(@RequestBody @Valid ClubActionByAdminRequest clubActionByAdminRequest) {
        String message = clubService.leaveClubByAdmin(clubActionByAdminRequest);
        return Map.of("message", message);
    }
}