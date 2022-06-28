package com.example.security.auth;

import com.example.security.model.Account;
import com.example.security.repository.AccountRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {


    private final AccountRespository accountRespository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRespository.findByUsername(username);
        if(account == null){
            throw new UsernameNotFoundException("user not found");
        }
        return new PrincipalDetails(account);
    }
}
