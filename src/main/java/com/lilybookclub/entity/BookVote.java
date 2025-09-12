package com.lilybookclub.entity;

import com.lilybookclub.enums.Vote;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_vote", uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "book_id"}) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookVote extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private Vote vote;
}
