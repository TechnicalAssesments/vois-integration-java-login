package com.example.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_info")
@Getter
@Setter
public class UserProfile {

    @Id
    private String username;
    @Transient
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;
}
