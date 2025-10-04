package com.lilybookclub.service.impl;

import com.lilybookclub.dto.request.user.LoginRequest;
import com.lilybookclub.dto.request.user.SignUpRequest;
import com.lilybookclub.dto.response.user.LoginResponse;
import com.lilybookclub.dto.response.user.SignUpResponse;
import com.lilybookclub.entity.User;
import com.lilybookclub.exception.BadRequestException;
import com.lilybookclub.exception.NotFoundException;
import com.lilybookclub.mapper.UserMapper;
import com.lilybookclub.repository.UserRepository;
import com.lilybookclub.security.jwt.JwtService;
import com.lilybookclub.util.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    private UserDetails usersDetails;

    @Mock
    private  UserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest loginRequest;
    private User mockedUser;

    @BeforeEach
    void setUp() {
        loginRequest = TestUtil.loginRequest();
        mockedUser = TestUtil.mockedUser();
    }

//    @Test
//    void createUserSuccessfulTest() {
//        Mockito.doReturn(false)
//                .when(userRepository).existsByEmail(anyString());
//        Mockito.doReturn(mockedUser)
//                .when(userRepository).save(any(User.class));
//
//        SignUpResponse response = userService.signUp(signUpRequest);
//
//        Assertions.assertEquals("LILY", response.getFirstname());
//
//        verify(userRepository).save(any(User.class));
//        verify(userRepository).existsByEmail(anyString());
//    }
//
//
//    @Test
//    void createUserFails_whenEmailExistsAlready() {
//        when(userRepository.existsByEmail(anyString()))
//                .thenReturn(true);
//
//        assertThatThrownBy(() -> userService.signUp(signUpRequest))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessage("An account with this email address already exists");
//    }


  @Test
    void loginFails_whenEmailDoesNotExist() {
        when(userRepository.findByEmail(loginRequest.getNullableEmail()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Account with email address not found");

    }

    @Test
    void authenticateUser_successfulLogin() {
            when(userRepository.findByEmail(loginRequest.getNullableEmail()))
                    .thenReturn(Optional.of(mockedUser));

        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);

        UserDetails mockUserDetails = mock(UserDetails.class);
            when(userDetailsService.loadUserByUsername(loginRequest.getNullableEmail()))
                    .thenReturn(mockUserDetails);

            when(jwtService.generateToken(mockUserDetails))
                    .thenReturn("ey132jnjaejbvehj55nmdfn44");

            LoginResponse response = authService.login(loginRequest);

            assertThat(response).isNotNull();
            assertThat(response.getToken()).isEqualTo("ey132jnjaejbvehj55nmdfn44");

            verify(userRepository).findByEmail(loginRequest.getNullableEmail());
            verify(userDetailsService).loadUserByUsername(loginRequest.getNullableEmail());
            verify(jwtService).generateToken(mockUserDetails);
        }

    }
