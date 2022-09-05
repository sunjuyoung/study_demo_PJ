package com.example.demo.entity;

import com.example.demo.entity.constant.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "member")
@ToString
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    protected Member(){

    }


    public Member(String email, String password, String address,  PasswordEncoder passwordEncoder) {
        this.email = email;
        this.password = passwordEncoder.encode(password);
        this.address = address;
        this.role = Role.USER;
    }

    public void changeRoleByAdmin(){
        this.role = Role.ADMIN;
    }

}
