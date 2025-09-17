package com.lilybookclub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "club_book", uniqueConstraints = { @UniqueConstraint(columnNames = {"club_id", "book_id"}) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubBook extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}