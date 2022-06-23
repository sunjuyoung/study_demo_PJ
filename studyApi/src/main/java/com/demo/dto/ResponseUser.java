package com.demo.dto;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class ResponseUser {


    private String email;
    private String nickname;

    private boolean emailVerified; //이메일 인증 완료 여부 확인

    private String emailCheckToken;

    private LocalDateTime joinedAt;

    private String bio;
    private String url;
    private String occupation;
    private String location;

}
