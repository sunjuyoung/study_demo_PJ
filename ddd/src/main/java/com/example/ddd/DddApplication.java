package com.example.ddd;

import com.example.ddd.test.RunClass;
import com.example.ddd.test.TestA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DddApplication {

    public static void main(String[] args) {
        SpringApplication.run(DddApplication.class, args);

        RunClass runClass = new RunClass(new TestA());
        runClass.gogo();

    }

}
