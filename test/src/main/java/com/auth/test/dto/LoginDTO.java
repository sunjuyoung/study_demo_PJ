package com.auth.test.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private String token;
    private String nickname;
    private String password;
    private String email;

}
