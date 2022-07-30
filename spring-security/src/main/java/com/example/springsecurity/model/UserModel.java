package com.example.springsecurity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private String nickName;
    private String email;
    private String password;
    private String matchingPassword;
}
