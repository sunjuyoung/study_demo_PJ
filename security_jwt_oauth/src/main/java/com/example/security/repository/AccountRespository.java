package com.example.security.repository;

import com.example.security.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRespository extends JpaRepository<Account,Long> {

    Account findByUsername(String username);
    Account findByEmail(String email);
    Boolean existsByUsername(String username);
}
