package com.auth.test.dto;

import lombok.Data;

@Data
public class SignUpForm {

    private String token;
    private String nickname;
    private String password;
    private String email;
    
}
