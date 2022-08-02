package com.demo;

import com.demo.domain.Role;
import com.demo.domain.User;
import com.demo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService service){
        return args -> {
            service.saveRole(new Role(null,"ROLE_USER"));
            service.saveRole(new Role(null,"ROLE_ADMIN"));

            service.saveUser(new User("sun","sun","1234","test@test.com", new ArrayList<>()));
            service.saveUser(new User("admin","admin","1234","admin@test.com", new ArrayList<>()));

            service.addRoleToUser("sun","ROLE_USER");
            service.addRoleToUser("admin","ROLE_ADMIN");
        };
    }
}
