package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_auth")
@Getter
@Setter
public class UserAuth {

    @Id
    private String username;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private String salt;
}
