package com.lilybookclub.entity;

import com.lilybookclub.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{
     @Column(unique = true, nullable = false, length = 100)
     private String email;
     @Column(nullable = false)
     private String password;
     @Column()
     private String firstname;
     @Column()
     private Role role;
}

