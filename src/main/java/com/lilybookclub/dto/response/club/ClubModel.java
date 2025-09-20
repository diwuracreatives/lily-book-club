package com.lilybookclub.dto.response.club;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubModel {
    private String name;
    private String category;
    private String readingDay;
    private String description;
    private long membersCount;
}



