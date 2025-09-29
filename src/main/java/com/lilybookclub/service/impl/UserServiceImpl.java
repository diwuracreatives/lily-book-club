package com.lilybookclub.service.impl;

import com.lilybookclub.config.AppConfig;
import com.lilybookclub.dto.request.user.SignUpRequest;
import com.lilybookclub.dto.response.user.SignUpResponse;
import com.lilybookclub.entity.User;
import com.lilybookclub.enums.Role;
import com.lilybookclub.exception.BadRequestException;
import com.lilybookclub.mapper.UserMapper;
import com.lilybookclub.repository.UserRepository;
import com.lilybookclub.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AppConfig appConfig;


    private User createUser(SignUpRequest signUpRequest, Role role){

        boolean userAccountExist = userRepository.existsByEmail(signUpRequest.getNullableEmail());

        if (userAccountExist){
            throw new BadRequestException("An account with this email address already exists");
        }

        String encodedPassword = passwordEncoder.encode(signUpRequest.getNullablePassword());

        User user = User.builder()
                .email(signUpRequest.getNullableEmail())
                .firstname(signUpRequest.getNullableFirstname())
                .lastname(signUpRequest.getNullableLastname())
                .password(encodedPassword)
                .role(role)
                .build();
        userRepository.save(user);
        return user;
    }

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest){
        User user = createUser(signUpRequest, Role.USER);
        return userMapper.toResponse(user);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void createDefaultAdminUsers() {
        for (SignUpRequest signUpRequest : appConfig.adminUsers()){
             try {
                 createUser(signUpRequest, Role.ADMIN);
                 log.info("Created default admin user with email {}",  signUpRequest.getNullableEmail());
             } catch (Exception e) {
                  log.error ("Error creating default admin account for {}", signUpRequest.getNullableEmail(), e);
             }
        }
    }


}