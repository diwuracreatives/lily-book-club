package com.lilybookclub.service.impl;


import com.lilybookclub.dto.request.club.CreateClubRequest;
import com.lilybookclub.dto.response.club.ClubModel;
import com.lilybookclub.entity.Club;
import com.lilybookclub.enums.DayOfTheWeek;
import com.lilybookclub.exception.BadRequestException;
import com.lilybookclub.mapper.ClubMapper;
import com.lilybookclub.repository.ClubRepository;
import com.lilybookclub.util.AppUtil;
import com.lilybookclub.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClubServiceImplTest {
    @Mock
    private ClubRepository clubRepository;

    @Mock
    private ClubMapper clubMapper;

    @InjectMocks
    private ClubServiceImpl clubService;

    private CreateClubRequest createClubRequest;

    @BeforeEach
    void setUp() {
        createClubRequest = TestUtil.createClubRequest();
    }

//    @Test
//    void createClub_successful() {
//
//        when(clubRepository.existsByCode(createClubRequest.getNullableCode()))
//                .thenReturn(false);
//
//        try (MockedStatic<AppUtil> mockedUtil = mockStatic(AppUtil.class)) {
//            mockedUtil.when(AppUtil::generateReadingDay)
//                    .thenReturn(DayOfTheWeek.MONDAY);
//
//            when(clubRepository.save(any(Club.class)))
//                    .thenAnswer(invocation -> invocation.getArgument(0));
//
//            ClubModel response = clubService.createClub(createClubRequest);
//
//            assertNotNull(response);
//            assertEquals(createClubRequest.getNullableName(), response.getName());
//            assertEquals(createClubRequest.getNullableCode(), response.getCode());
//            assertEquals(createClubRequest.getCategory(), response.getCategory());
//            assertEquals(DayOfTheWeek.MONDAY.name(), response.getReadingDay());
//            assertEquals(createClubRequest.getNullableDescription(), response.getDescription());
//
//            verify(clubRepository).existsByCode(createClubRequest.getNullableCode());
//            verify(clubRepository).save(any(Club.class));
//
//        }
//    }

    @Test
    void createClub_throwsBadRequest_whenClubCodeAlreadyExists() {
        when(clubRepository.existsByCode(createClubRequest.getNullableCode()))
                .thenReturn(true);

        assertThatThrownBy(() -> clubService.createClub(createClubRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Club with this code already exists");

        verify(clubRepository, never()).save(any());
    }





}