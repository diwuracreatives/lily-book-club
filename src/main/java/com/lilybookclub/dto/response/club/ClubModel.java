package com.lilybookclub.dto.response.club;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lilybookclub.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClubModel {

    private String name;

    private String code;

    private Category category;

    private String readingDay;

    private String description;

    private Long membersCount;
}



