package com.lilybookclub.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "user_clubs", uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "club_id"}) })
@SQLRestriction("is_deleted = false")
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


