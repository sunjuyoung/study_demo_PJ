package com.example.security.controller;

import com.example.security.model.Account;
import com.example.security.repository.AccountRespository;
import com.example.security.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final AccountRespository accountRespository;
    private final AccountService accountService;


    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }
    
    @GetMapping("/signUp")
    public String signUpForm(Model model){
        model.addAttribute(new Account());
        return "signUp";
    }
    @PostMapping("/signUp")
    public String submitSignUp(@ModelAttribute Account account){
        accountService.saveAccount(account,"ROLE_USER");
        return "redirect:/login";
    }

    @GetMapping("/signupProc")
    public @ResponseBody String signupProc(){
        return "회원가입 완료됨";
    }

    @GetMapping("/user")
    public String user(){
        return "user";
    }
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }


/*    @GetMapping("/info")
    @Secured("ROLE_MANGER")
    public @ResponseBody String info(){
        return "개인정보";
    }*/
    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public @ResponseBody String info(){
        return "개인정보";
    }
}
