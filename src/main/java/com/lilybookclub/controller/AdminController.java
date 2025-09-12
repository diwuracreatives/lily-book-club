package com.lilybookclub.controller;

import com.lilybookclub.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


    @GetMapping
    public String AdminGreeting(){
        return adminService.AdminGreeting();
    }
}
