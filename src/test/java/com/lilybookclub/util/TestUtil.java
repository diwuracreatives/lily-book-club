package com.lilybookclub.util;

import com.lilybookclub.dto.request.book.CreateBookRequest;
import com.lilybookclub.dto.request.club.CreateClubRequest;
import com.lilybookclub.dto.request.user.LoginRequest;
import com.lilybookclub.dto.request.user.SignUpRequest;
import com.lilybookclub.entity.Club;
import com.lilybookclub.entity.User;
import com.lilybookclub.enums.Category;
import com.lilybookclub.enums.DayOfTheWeek;

import static com.lilybookclub.enums.Role.USER;

public class TestUtil {

    public static SignUpRequest signUpRequest () {
        return SignUpRequest.builder()
                .email("lily@gmail.com")
                .password("lily4567")
                .firstname("Lily")
                .lastname("Mary")
                .build();
    }

    public static CreateClubRequest createClubRequest () {
        return CreateClubRequest.builder()
                .name("test")
                .code("Cl0000")
                .category(Category.ARTS)
                .description("testing club")
                .build();
    }

    public static User mockedUser () {
        return User.builder()
                .firstname("Lily")
                .lastname("Mary")
                .role(USER)
                .email("lily@gmail.com")
                .password("lily4567")
                .role(USER)
                .build();
    }

    public static Club mockedClub () {
        return Club.builder()
                .name("test")
                .code("Cl0000")
                .category(Category.ARTS)
                .readingDay(DayOfTheWeek.MONDAY)
                .description("testing club")
                .build();
    }

    public static LoginRequest loginRequest () {
        return LoginRequest.builder()
                .email("lily@gmail.com")
                .password("lily4567")
                .build();
    }


}