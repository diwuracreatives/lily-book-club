package com.lilybookclub.entity;

import com.lilybookclub.enums.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clubs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Club extends BaseEntity{
    @Column(unique = true, nullable = false)
    private String code;
    private String name;
    private Integer readingDay;
    private Category category;
}
