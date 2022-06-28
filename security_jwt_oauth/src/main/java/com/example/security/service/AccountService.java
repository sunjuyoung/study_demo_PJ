package com.example.security.service;

import com.example.security.model.Account;
import com.example.security.repository.AccountRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRespository accountRespository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Transactional
    public void saveAccount(Account account,String role){
        account.setRole(role);
        account.setCreatedAt(LocalDateTime.now());
        String pw = bCryptPasswordEncoder.encode(account.getPassword());
        account.setPassword(pw);
        accountRespository.save(account);
    }
}
