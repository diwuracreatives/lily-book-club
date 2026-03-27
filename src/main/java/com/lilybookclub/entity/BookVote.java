package com.lilybookclub.entity;

import com.lilybookclub.enums.Vote;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_votes", uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "book_id"}) })
@Getter
@Setter
@Builder
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

