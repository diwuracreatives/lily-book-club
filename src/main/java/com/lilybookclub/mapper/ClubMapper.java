package com.lilybookclub.mapper;

import com.lilybookclub.dto.response.club.ClubModel;
import com.lilybookclub.entity.Club;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClubMapper {

    @Mapping(target = "membersCount", source = "membersCount")
    ClubModel toResponse(Club club, Long membersCount);

}

