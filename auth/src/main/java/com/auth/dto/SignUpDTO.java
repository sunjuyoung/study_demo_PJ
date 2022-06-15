package com.auth.dto;

import com.auth.entity.Member;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class SignUpDTO {
    private String token;

    @NotBlank(message = " 필수 ")
    private String nickname;
    @NotBlank(message = "비밀번호는 필수 입력입니다")
    private String password;
    @NotBlank(message = "이메일은 필수 입력입니다")
    @Email
    private String email;

    private Role role;


    public  Member createUserSave(SignUpDTO signUpDTO, PasswordEncoder passwordEncoder){

        Member member = new Member();
        member.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        member.setRole(Role.USER);
        member.setEmail(signUpDTO.getEmail());
        member.setNickname(signUpDTO.getNickname());

        return member;

    }
}
