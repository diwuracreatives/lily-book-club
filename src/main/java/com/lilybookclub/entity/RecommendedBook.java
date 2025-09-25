package com.lilybookclub.entity;

import com.lilybookclub.enums.BookApprovalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "recommended_books", uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "club_id"}) })
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedBook extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private BookApprovalStatus bookApprovalStatus;

    private String title;

    private String author;

    private String link;

    private String imageUrl;

    private String description;
}

