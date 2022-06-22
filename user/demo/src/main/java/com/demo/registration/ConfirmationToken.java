package com.demo.registration;

import com.demo.user.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ConfirmationToken {

    @Id
    @SequenceGenerator(
            name = "confirm_token_sequence",
            sequenceName = "confirm_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "confirm_token_sequence")
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public ConfirmationToken(String token, LocalDateTime createdAt,
                             LocalDateTime expiredAt, LocalDateTime confirmedAt, AppUser appUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.confirmedAt = confirmedAt;
        this.appUser = appUser;
    }
}
