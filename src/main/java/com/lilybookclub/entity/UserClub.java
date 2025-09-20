package com.lilybookclub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_clubs", uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "club_id"}) })
@Getter
@Setter
@Builder
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


