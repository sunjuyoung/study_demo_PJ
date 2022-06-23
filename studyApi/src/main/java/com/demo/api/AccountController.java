package com.demo.api;

import com.demo.dto.ResponseUser;
import com.demo.dto.SignUpDTO;
import com.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @PostMapping("/save")
    public ResponseEntity<ResponseUser> saveUser(@RequestBody SignUpDTO signUpDTO){
        ResponseUser responseUser = accountService.saveUser(signUpDTO);
        return ResponseEntity.ok().body(responseUser);
    }
}
