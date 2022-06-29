package com.example.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private String role;

    private String provider;
    private String providerId;

    private LocalDateTime createdAt;

    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    @Builder
    public Account(String username, String password, String email, String role,
                   String provider, String providerId,LocalDateTime createdAt) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createdAt = createdAt;
    }
}
