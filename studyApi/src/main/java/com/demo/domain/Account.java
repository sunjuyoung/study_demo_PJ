package com.demo.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified = false;

    private String emailCheckToken;

    private LocalDateTime joinedAt;

    private String bio;
    private String url;
    private String occupation;
    private String location;


    @ManyToMany
    private List<Role> roles = new ArrayList<>();


    public void generateToken(){
        this.emailCheckToken = UUID.randomUUID().toString();
    }

    public void completeSignUp(){
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

}
