package com.lilybookclub.dto.response.club;

import com.lilybookclub.entity.Club;

public interface ClubWithMemberCount {
    Club getClub();
    Long getMemberCount();
}



