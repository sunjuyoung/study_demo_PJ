package com.project.study.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id @GeneratedValue
    private Long id;


    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified; //이메일 인증 완료 여부 확인

    private String emailCheckToken;

    private LocalDateTime joinedAt;

    private String bio;
    private String url;
    private String occupation;
    private String location;

    @Lob
    private String profileImage;


    //알림
    private boolean studyCreatedByEmail; //스터디 개설
    private boolean studyCreatedByWeb = true;

    private boolean studyEnrollmentResultByEmail; //스터디 가입신청결과
    private boolean studyEnrollmentResultByWeb  = true;

    private boolean studyUpdatedByWeb  = true;
    private boolean studyUpdatedByEmail;


    public void generateToken() {
        this.emailCheckToken= UUID.randomUUID().toString();
    }
}
