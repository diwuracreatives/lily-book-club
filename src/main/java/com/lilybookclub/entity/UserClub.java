package com.lilybookclub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_club", uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "club_id"}) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserClub extends BaseEntity{
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne
        @JoinColumn(name = "club_id")
        private Club club;
}


