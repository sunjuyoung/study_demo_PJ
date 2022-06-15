package com.auth.test.service;

import com.auth.test.auth.UserAccount;
import com.auth.test.dto.LoginDTO;
import com.auth.test.dto.SignUpForm;
import com.auth.test.entity.Account;
import com.auth.test.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Transactional
    public Account saveNewAccount(SignUpForm signUpForm) {
        Account byNickname = accountRepository.findByNickname(signUpForm.getNickname());
        if(byNickname !=null){
            throw new IllegalStateException("이미 가입된 회원입니다");
        }
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .build();
        Account save = accountRepository.save(account);
        return save;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByNickname(username);
        if(account == null){
            throw new UsernameNotFoundException("no user!!!!!!!!!!!!!!!");
        }else{
            System.out.println("@#####$@$@#$@#$@#$@#$@#$@#$");
        }
        return new UserAccount(account);
    }


    public Account apiCreate(SignUpForm signUpForm) {
        Account account = modelMapper.map(signUpForm, Account.class);
        if(accountRepository.existsByEmail(account.getEmail())){
            throw new RuntimeException("email already exist");
        }
        account.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        return accountRepository.save(account);

    }

    public Account getUser(LoginDTO loginDTO) {
        Account account = accountRepository.findByNickname(loginDTO.getNickname());

        if(account !=null && passwordEncoder.matches(loginDTO.getPassword(),account.getPassword())){
            return account;
        }
        return  null;

    }
}
