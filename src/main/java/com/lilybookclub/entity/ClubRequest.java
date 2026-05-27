package com.lilybookclub.entity;

import com.lilybookclub.enums.Status;
import com.lilybookclub.enums.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "club_requests")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubRequest extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String code;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String description;

    @Column(name = "approval_status")
    @Enumerated(EnumType.STRING)
    private Status clubApprovalStatus;
}
