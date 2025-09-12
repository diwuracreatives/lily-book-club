package com.lilybookclub.service.impl;

import com.lilybookclub.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    public String Greeting(){
        return "Welcome to Lily Book Club 🌸";
    }
}
