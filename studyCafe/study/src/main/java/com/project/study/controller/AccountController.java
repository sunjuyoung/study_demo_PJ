package com.project.study.controller;

import com.project.study.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountController {

    @GetMapping("/sign-up")
    public String signUpForm(Model model){

        model.addAttribute("signUpForm",new SignUpDto());
        return "account/sign-up";
    }




}
