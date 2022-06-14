package com.auth.test.controller;

import com.auth.test.auth.TokenProvider;
import com.auth.test.dto.LoginDTO;
import com.auth.test.dto.SignUpForm;
import com.auth.test.dto.UserDTO;
import com.auth.test.entity.Account;
import com.auth.test.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountApiController {

    private final AccountService accountService;
    private final ModelMapper modelMapper;
    private final TokenProvider tokenProvider;

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@RequestBody SignUpForm signUpForm){
        try{
           Account account =  accountService.apiCreate(signUpForm);
            LoginDTO dto = modelMapper.map(account, LoginDTO.class);
            return ResponseEntity.ok().body(dto);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(signUpForm);
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        Account account = accountService.getUser(loginDTO);

        String token = tokenProvider.create(account);
        UserDTO responseDTO = UserDTO.builder()
                .email(account.getEmail())
                .token(token)
                .username(account.getNickname())
                .build();


        return ResponseEntity.badRequest().body(responseDTO);
    }



}
