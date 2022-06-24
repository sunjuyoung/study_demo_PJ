package com.demo;

import com.demo.domain.Role;
import com.demo.dto.SignUpDTO;
import com.demo.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    CommandLineRunner run(AccountService accountService){
        return args -> {

            accountService.saveRole(new Role(null,"ROLE_USER"));
            accountService.saveRole(new Role(null,"ROLE_ADMIN"));

            accountService.saveUser(new SignUpDTO("sun","1234","test@test.com"));
            accountService.saveUser(new SignUpDTO("admin","1234","admin@test.com"));
            accountService.addRoleToUser("sun","ROLE_USER");
            accountService.addRoleToUser("admin","ROLE_ADMIN");

        };

    }
}
