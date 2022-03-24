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
@Builder
public class Notification {

    @Id @GeneratedValue
    private Long id;

    private String  title;

    private String link;

    private String message;

    private boolean checked;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    private LocalDateTime createdLocalDateTime;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;



}
