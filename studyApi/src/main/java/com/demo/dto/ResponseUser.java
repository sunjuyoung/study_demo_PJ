package com.demo.dto;

import com.demo.domain.Role;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<Role> roles = new ArrayList<>();

}
