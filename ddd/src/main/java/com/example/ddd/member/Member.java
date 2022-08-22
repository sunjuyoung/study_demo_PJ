package com.example.ddd.member;

import com.example.ddd.order.Order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String password;

    private boolean blocked;

    private String email;



}
