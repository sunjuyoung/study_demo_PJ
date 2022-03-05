package com.project.study.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Account {

    @Id
    @GeneratedValue
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

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();


    //알림
    private boolean studyCreatedByEmail; //스터디 개설
    private boolean studyCreatedByWeb = true;

    private boolean studyEnrollmentResultByEmail; //스터디 가입신청결과
    private boolean studyEnrollmentResultByWeb = true;

    private boolean studyUpdatedByWeb = true;
    private boolean studyUpdatedByEmail;

    private LocalDateTime emailCheckAt;
    private Integer countConfirmEmail;

    public void generateToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckAt = LocalDateTime.now();
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
        this.countConfirmEmail = 1;
    }

    public boolean resendConfirmEmail() {
        return this.emailCheckAt.isBefore(LocalDateTime.now().minusMinutes(15));
    }

    public boolean checkSendConfirmEmail() {
        if (this.countConfirmEmail !=null &&this.countConfirmEmail >= 2) {
            if (resendConfirmEmail()) {
                this.countConfirmEmail = 0;
            }
            this.emailCheckAt = LocalDateTime.now();
            return false;
        } else {
            if(this.countConfirmEmail == null){
                this.countConfirmEmail =1;
            }else {
                this.countConfirmEmail++;
            }
            return true;
        }
      //  return this.emailCheckAt.isBefore(LocalDateTime.now().minusMinutes(15));
    }

    public boolean isManager(Study study){
        return study.getManagers().contains(this);
    }
    public boolean isMembers(Study study){
        return study.getMembers().contains(this);
    }
}
