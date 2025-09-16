package com.lilybookclub.controller;

import com.lilybookclub.dto.request.club.ClubActionByAdminRequest;
import com.lilybookclub.dto.request.club.ClubActionByUserRequest;
import com.lilybookclub.dto.request.club.CreateClubRequest;
import com.lilybookclub.dto.response.user.ClubModel;
import com.lilybookclub.service.ClubService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/clubs")
@RestController
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClubModel> CreateClub(@RequestBody @Valid CreateClubRequest createClubRequest) {
        ClubModel clubModel = clubService.CreateClub(createClubRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(clubModel);
    }

    @GetMapping("")
    public ResponseEntity<List<ClubModel>> GetClubs() {
        List<ClubModel> clubModels = clubService.GetClubs();
        return ResponseEntity.ok().body(clubModels);
    }

    @GetMapping("/categories")
    public ResponseEntity<ClubModel> GetClubByCategory(@RequestParam("category") String category) {
        ClubModel clubModel = clubService.GetClubByCategory(category);
        return ResponseEntity.ok().body(clubModel);
    }

    @PostMapping("/users")
    public ResponseEntity<ClubModel> JoinClubByUser(@RequestBody @Valid ClubActionByUserRequest clubActionByUserRequest) {
        ClubModel clubModel = clubService.JoinClubByUser(clubActionByUserRequest);
        return ResponseEntity.ok().body(clubModel);
    }

    @DeleteMapping("/users")
    public ResponseEntity<Map<String, String>> LeaveClubByUser(@RequestBody @Valid ClubActionByUserRequest clubActionByUserRequest) {
        String message = clubService.LeaveClubByUser(clubActionByUserRequest);
        return ResponseEntity.ok().body(Map.of("message", message));
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClubModel> JoinClubByAdmin(@RequestBody @Valid ClubActionByAdminRequest clubActionByAdminRequest) {
        ClubModel clubModel = clubService.JoinClubByAdmin(clubActionByAdminRequest);
        return ResponseEntity.ok().body(clubModel);
    }

    @DeleteMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> LeaveClubByAdmin(@RequestBody @Valid ClubActionByAdminRequest clubActionByAdminRequest) {
        String message = clubService.LeaveClubByAdmin(clubActionByAdminRequest);
        return ResponseEntity.ok().body(Map.of("message", message));
    }
}