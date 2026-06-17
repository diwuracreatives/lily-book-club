package com.lilybookclub.entity;

import com.lilybookclub.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "book_requests")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private Status bookApprovalStatus;

    private String title;

    private String author;

    private String link;

    private String imageUrl;

    private String description;
}

