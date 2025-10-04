package com.lilybookclub.controller;

import com.lilybookclub.dto.request.club.ClubActionByAdminRequest;
import com.lilybookclub.dto.request.club.ClubInterestRequest;
import com.lilybookclub.dto.request.club.CreateClubRequest;
import com.lilybookclub.dto.response.club.ClubModel;
import com.lilybookclub.enums.Category;
import com.lilybookclub.service.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/clubs")
@RestController
@RequiredArgsConstructor
@Tag(name = "Club Management", description = "Endpoints for managing club by user and admin")
@SecurityRequirement(name="Bearer Authentication")
public class ClubController {

    private final ClubService clubService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a club", description = "Create a new club")
    public ClubModel createClub(@RequestBody @Valid CreateClubRequest createClubRequest) {
        return clubService.createClub(createClubRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all clubs", description = "See all clubs available to join")
    public Page<ClubModel> getClubs(@RequestParam(value = "categories", required = false) List<Category> categories, @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return clubService.getClubs(categories, pageable);
    }

    @GetMapping("/{clubCode}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a club", description = "Get a club by club code")
    public ClubModel getClubByCode(@PathVariable String clubCode) {
        return clubService.getClubByCode(clubCode);
    }

    @PostMapping("/ai-suggestion")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all suggested club", description = "See all clubs suggested by gemini AI by interest")
    public Page<ClubModel> getSuggestedClubs(@RequestBody @Valid ClubInterestRequest clubInterestRequest, @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return clubService.getClubsByInterest(clubInterestRequest, pageable);
    }

    @PostMapping("/{clubCode}/join")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Join a club", description = "Join a club by club code")
    public ClubModel joinClubByUser(@PathVariable String clubCode) {
        return clubService.joinClubByUser(clubCode);
    }

    @DeleteMapping("/{clubCode}/leave")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Leave a club", description = "Leave a club by club code")
    public Map<String, String> leaveClubByUser(@PathVariable String clubCode) {
        String message = clubService.leaveClubByUser(clubCode);
        return Map.of("message", message);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add user to a club", description = "Add user to a club by admin")
    public ClubModel joinClubByAdmin(@RequestBody @Valid ClubActionByAdminRequest clubActionByAdminRequest) {
        return  clubService.joinClubByAdmin(clubActionByAdminRequest);
    }

    @DeleteMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Remove user from a club", description = "Remove user from a club by admin")
    public Map<String, String> leaveClubByAdmin(@RequestBody @Valid ClubActionByAdminRequest clubActionByAdminRequest) {
        String message = clubService.leaveClubByAdmin(clubActionByAdminRequest);
        return Map.of("message", message);
    }
}