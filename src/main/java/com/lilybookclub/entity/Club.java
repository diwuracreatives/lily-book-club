package com.lilybookclub.entity;

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
    private String clubId;
    @Column()
    private String name;
    @Column()
    private Integer readingDay;
    @Column()
    private Integer members;
    @Column()
    private Integer category;
    @Column()
    private Boolean active;
}


