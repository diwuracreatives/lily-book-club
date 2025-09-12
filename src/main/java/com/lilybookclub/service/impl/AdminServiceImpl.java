package com.lilybookclub.service.impl;

import com.lilybookclub.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    public String AdminGreeting(){
        return "Admin, Welcome to Lily Book Club 🌸";
    }
}
