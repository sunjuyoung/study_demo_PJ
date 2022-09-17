package com.example.datajpa.repository;

import com.example.datajpa.entity.Guardian;
import com.example.datajpa.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void test() throws Exception{
        //given
        Guardian guardian = Guardian.builder()
                .email("g1@test.com")
                .name("g1")
                .mobile("112")
                .build();

        Student student = Student.builder()
        .emailId("test@test.com")
        .firstName("sun")
        .lastName("ju")
        .guardian(guardian)
                .build();

        studentRepository.save(student);
    }


}