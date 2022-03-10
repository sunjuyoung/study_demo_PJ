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
        return !createBy.equals(userAccount) && enrollments.stream().filter(i->i.getAccount().equals(userAccount)).count() >0;

    }
    public boolean isDisenrollableFor(UserAccount userAccount){
        return createBy.equals(userAccount);
    }
    public boolean isAttended(UserAccount userAccount){
        return createBy.equals(userAccount);
    }



}
