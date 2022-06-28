package com.example.security;

import com.example.security.model.Account;
import com.example.security.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(AccountService service){
        return args -> {

            service.saveAccount(new Account("sun","12341234","test@test.com"), "ROLE_USER");
            service.saveAccount(new Account("admin","12341234","admin@test.com"), "ROLE_ADMIN");
            service.saveAccount(new Account("manager","12341234","manager@test.com"), "ROLE_MANAGER");

        };
    }
}
