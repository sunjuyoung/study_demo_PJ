package com.project.study.domain;

import com.project.study.auth.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return !createBy.equals(userAccount.getAccount()) && !isAlreadyEnrolled(userAccount.getAccount()) && isNotClosed();

    }
    public boolean isDisenrollableFor(UserAccount userAccount){
        return !createBy.equals(userAccount.getAccount()) && isNotClosed() && isAttended(userAccount);
    }
    public boolean isAttended(UserAccount userAccount){
        Optional<Enrollment> en = this.enrollments.stream().filter(i->i.getAccount().equals(userAccount.getAccount())).filter(Enrollment::isAttended).findFirst();
            if(en.isPresent()){
                return en.get().isAttended();
            }else {
                return false;
            }
    }

    public boolean isEventManager(UserAccount userAccount){
        return createBy.equals(userAccount.getAccount());
    }

    public boolean isAbleToAcceptEnrollment(){
        return this.eventType == EventType.FCFS && numberOfRemainSpots() >0;
    }

    //모임에 참가중
    public boolean isAlreadyEnrolled(Account account){
        return enrollments.stream().filter(i->i.getAccount().equals(account)).count() >0;
    }
    //모임 중인지 마감일 확인
    public boolean isNotClosed(){
        return endEnrollmentDateTime.isAfter(LocalDateTime.now());
    }

    public int numberOfRemainSpots(){
        return limitOfEnrollment - (int) enrollments.stream().filter(i->i.isAccepted()).count();
    }

    public boolean canAccept(Enrollment enrollment){
        return this.eventType == EventType.CONFIRM
                && this.enrollments.contains(enrollment)
                && !enrollment.isAttended();
    }

    public boolean canReject(Enrollment enrollment){
        return this.eventType == EventType.CONFIRM
                && this.enrollments.contains(enrollment)
                && !enrollment.isAttended();
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
        enrollment.setEvent(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
        enrollment.setEvent(null);
    }
}
