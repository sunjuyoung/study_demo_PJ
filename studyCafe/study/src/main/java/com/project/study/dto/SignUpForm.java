package com.project.study.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {

    @NotBlank
    @Length(min = 3,max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9]{3,20}$",message = "3~20자의 영문 소문자,숫자를 입력해주세요, 특수 기호는 불가능합니다.")
    private String nickname;

    @NotBlank
    @Length(min = 8,max = 50)
    private String password;

    @Email
    @NotBlank
    private String email;
}
