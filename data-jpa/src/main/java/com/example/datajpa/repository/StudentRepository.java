package com.example.datajpa.repository;


import com.example.datajpa.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {

     List<Student> findByFirstName(String firstName);

     List<Student> findByFirstNameContaining(String firstName);

    List<Student> findByLastNameNotNull();

    List<Student> findByGuardianName(String guardianName);
}
