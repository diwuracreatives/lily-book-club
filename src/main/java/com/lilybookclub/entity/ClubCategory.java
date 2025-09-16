package com.lilybookclub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Table(name = "club_categories")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubCategory extends BaseEntity {
      private String name;
}
