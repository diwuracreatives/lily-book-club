package com.lilybookclub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {

    private String title;

    private String author;

    private String link;

    private String imageUrl;

    private String description;
}




