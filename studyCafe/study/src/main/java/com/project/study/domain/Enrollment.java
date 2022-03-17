package com.project.study.domain;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Enrollment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @ManyToOne
    private Account account;

    private LocalDateTime enrolledAt;

    private boolean accepted;

    private boolean attended;


    public void newEvent(Account account,Event event) {
        this.account = account;
        this.event = event;
        this.enrolledAt = LocalDateTime.now();
        this.accepted = true;
        this.attended = true;
    }
}
