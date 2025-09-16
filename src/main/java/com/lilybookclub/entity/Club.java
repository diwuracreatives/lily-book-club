package com.lilybookclub.entity;

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
    @Column(unique = true, nullable = false)
    private String code;
    private String name;
    private Integer readingDay;

    @OneToOne
    @JoinColumn(name="category_id", nullable = false)
    private ClubCategory category;
}
