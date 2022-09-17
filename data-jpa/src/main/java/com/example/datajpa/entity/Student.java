package com.example.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "student",
        uniqueConstraints = @UniqueConstraint(
                name = "emailId_unique",
                columnNames = "email_address"
        )
)
public class Student {

    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "student_sequence")
    private Long studentId;
    private String firstName;
    private String lastName;

    @Column(name = "email_address",nullable = false)
    private String emailId;

    @Embedded
    private Guardian guardian;
}
