package com.project.study.domain;

import com.project.study.auth.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Study {

    @Id @GeneratedValue
    private Long id;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    @Lob
    private String fullDescription;

    @Lob
    private String image;

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    private LocalDateTime publishedDateTime;
    private LocalDateTime closedDateTime;
    private LocalDateTime recruitedUpdateDateTime;

    private boolean recruiting;
    private boolean published;
    private boolean closed;
    private boolean useBanner;


    public void addManager(Account account) {
        this.managers.add(account);
    }

    public boolean isJoinable(UserAccount userAccount){
        return this.published && this.recruiting;
    }
    public boolean isMember(UserAccount userAccount){
        return this.members.contains(userAccount.getAccount());
    }
    public boolean isManager(UserAccount userAccount){
        return  this.managers.contains(userAccount.getAccount());
    }

    public void addMember(Account account) {
        this.members.add(account);
    }
}
