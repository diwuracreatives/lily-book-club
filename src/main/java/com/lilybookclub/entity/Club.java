package com.lilybookclub.entity;

import com.lilybookclub.enums.Category;
import com.lilybookclub.enums.DayOfTheWeek;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clubs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Club extends BaseEntity{
    private String name;
    @Enumerated(EnumType.STRING)
    private DayOfTheWeek readingDay;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String description;

}

