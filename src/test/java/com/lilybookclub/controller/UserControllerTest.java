package com.lilybookclub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilybookclub.advice.ErrorControllerAdvice;
import com.lilybookclub.dto.request.user.SignUpRequest;
import com.lilybookclub.dto.response.user.SignUpResponse;

import com.lilybookclub.security.jwt.JwtAuthenticationFilter;
import com.lilybookclub.security.jwt.JwtService;
import com.lilybookclub.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Clock;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private SignUpRequest signUpRequest;

    private SignUpResponse signUpResponse;

    @MockitoBean
    private Clock clock;

    @MockitoBean
    private ErrorControllerAdvice errorControllerAdvice;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private JwtService jwtService;


    @BeforeEach
    public void setup() {
        signUpRequest = SignUpRequest.builder().email("a@m.com").firstname("lily").lastname("club").password("lily1234").build();
        signUpResponse = SignUpResponse.builder().email("a@m.com").firstname("lily").lastname("club").build();
    }

    @Test
    public void UserController_SignUp() throws Exception {
        when(userService.signUp(ArgumentMatchers.any(SignUpRequest.class)))
                .thenReturn(signUpResponse);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)));


        resultActions.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(signUpResponse.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", CoreMatchers.is(signUpResponse.getFirstname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", CoreMatchers.is(signUpResponse.getLastname())));

    }

}
//Arrange -> Act -> Assert -> Verify