package com.demo.service;

import com.demo.domain.Account;
import com.demo.dto.ResponseUser;
import com.demo.dto.SignUpDTO;
import com.demo.repo.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public ResponseUser getUser(String nickname) {
        Account account = accountRepository.findByNickname(nickname);
        if(account == null){
            throw new UsernameNotFoundException("user not found");
        }
        ResponseUser responseUser = modelMapper.map(account, ResponseUser.class);
        return null;
    }

    @Override
    public List<ResponseUser> findAllUsers() {
        List<Account> accounts = accountRepository.findAll();
        List<ResponseUser> responseUsers = new ArrayList<>();
        accounts.stream().map(account -> responseUsers.add(modelMapper.map(account,ResponseUser.class)));
        return responseUsers;
    }

    @Override
    public ResponseUser saveUser(SignUpDTO signUpDTO) {
        Account account = modelMapper.map(signUpDTO, Account.class);
        if(accountRepository.existsByNickname(account.getNickname())){
            throw new IllegalStateException("user already exists");
        }
        account.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        Account save = accountRepository.save(account);
        return modelMapper.map(save,ResponseUser.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
