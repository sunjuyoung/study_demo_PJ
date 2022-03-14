package com.project.study.domain;

import com.project.study.auth.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Event {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account createBy;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdDateTime;
    @Column(nullable = false)
    private LocalDateTime endEnrollmentDateTime;
    @Column(nullable = false)
    private LocalDateTime startDateTime;
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    private Integer limitOfEnrollment;

    @OneToMany(mappedBy = "event")
    private List<Enrollment> enrollments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EventType eventType;


    public boolean isEnrollableFor(UserAccount userAccount){

        return !createBy.equals(userAccount.getAccount()) && isAlreadyEnrolled(userAccount) && isNotClosed();

    }
    public boolean isDisenrollableFor(UserAccount userAccount){
        return createBy.equals(userAccount);
    }
    public boolean isAttended(UserAccount userAccount){
        return createBy.equals(userAccount);
    }


    //모임에 참가중
    public boolean isAlreadyEnrolled(UserAccount userAccount){
        Account account = userAccount.getAccount();
        return enrollments.stream().filter(i->i.getAccount().equals(account)).count() >0;
    }
    //모임 중인지 마감일 확인
    public boolean isNotClosed(){
        return endEnrollmentDateTime.isAfter(LocalDateTime.now());
    }

    public int numberOfRemainSpots(){
        return limitOfEnrollment - (int) enrollments.stream().filter(i->i.isAccepted()).count();
    }

}
