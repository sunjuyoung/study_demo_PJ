package com.demo.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpDTO {

    @NotBlank
    @Length(min = 3,max = 20)
    private String nickname;

    @NotBlank
    @Length(min = 8,max = 50)
    private String password;

    @Email
    @NotBlank
    private String email;


    public SignUpDTO( String nickname, String password, String email) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
    }
}
