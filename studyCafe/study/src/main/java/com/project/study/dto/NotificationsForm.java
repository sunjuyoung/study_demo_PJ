package com.project.study.dto;

import com.project.study.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationsForm {

    private boolean studyCreatedByEmail; //스터디 개설
    private boolean studyCreatedByWeb ;

    private boolean studyEnrollmentResultByEmail; //스터디 가입신청결과
    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpdatedByWeb;
    private boolean studyUpdatedByEmail;

}
