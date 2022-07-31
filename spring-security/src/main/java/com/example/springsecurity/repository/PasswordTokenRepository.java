package com.example.springsecurity.repository;

import com.example.springsecurity.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken,Long> {


    PasswordResetToken findByToken(String token);
}
