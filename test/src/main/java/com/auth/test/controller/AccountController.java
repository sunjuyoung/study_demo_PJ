package com.auth.test.controller;

import com.auth.test.dto.LoginDTO;
import com.auth.test.dto.SignUpForm;
import com.auth.test.service.AccountService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountController {


    private final AccountService accountService;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/signUp")
    public String signUp(Model model){
        model.addAttribute(new SignUpForm());

        return "signUp";
    }
    @PostMapping("/signUp")
    public String signUp(Model model, @ModelAttribute SignUpForm signUpForm){
        model.addAttribute(new SignUpForm());

        return "signUp";
    }


    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute(new LoginDTO());
        return "login";
    }
}
