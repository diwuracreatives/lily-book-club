package com.lilybookclub.service.impl;

import com.lilybookclub.dto.request.user.SignUpRequest;
import com.lilybookclub.dto.response.user.SignUpResponse;
import com.lilybookclub.entity.User;
import com.lilybookclub.enums.Role;
import com.lilybookclub.mapper.UserMapper;
import com.lilybookclub.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void UserService_CreateUser_ReturnsUser() {
        User user = User.builder()
                .email("oye@gmail.com")
                .password("oye20")
                .firstname("banji")
                .lastname("oye")
                .role(Role.USER)
                .build();

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("oye@gmail.com")
                .password("oye20")
                .firstname("banji")
                .lastname("oye")
                .build();

        SignUpResponse signUpResponse = SignUpResponse.builder()
                .email("oye@gmail.com")
                .firstname("banji")
                .lastname("oye")
                .build();

        when(userRepository.existsByEmail(signUpRequest.getNullableEmail())).thenReturn(false);
        when(passwordEncoder.encode(signUpRequest.getNullablePassword()))
                .thenReturn("encoded_password_123");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(signUpResponse);

        SignUpResponse newSignUpResponse = userService.signUp(signUpRequest);

        Assertions.assertNotNull(newSignUpResponse);
        Assertions.assertEquals(signUpResponse, newSignUpResponse);

        verify(userRepository, times(1)).save(any(User.class));
    }

}
