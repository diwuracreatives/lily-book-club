package com.lilybookclub.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "club_books", uniqueConstraints = { @UniqueConstraint(columnNames = {"club_id", "book_id"}) })
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubBook extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private LocalDateTime readDate;
}
