package com.lilybookclub.dto.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubModel {
    private String code;
    private String name;
    private String category;
    private String readingDay;
    private int membersCount;
}



