package com.auth.test.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "account")
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Builder
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname;

    private String password;

    @Column(unique = true)
    private String email;


}
