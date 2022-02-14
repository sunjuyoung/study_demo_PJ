package com.project.study.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true,nullable = false)
    private String title;
}
