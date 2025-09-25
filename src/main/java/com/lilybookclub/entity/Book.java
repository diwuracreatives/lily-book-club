package com.lilybookclub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "books")
@SQLRestriction("is_deleted = false")
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




