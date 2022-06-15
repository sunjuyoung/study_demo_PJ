package com.auth.controller;


import com.auth.dto.LoginDTO;
import com.auth.dto.SignUpDTO;
import com.auth.entity.Member;
import com.auth.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/signUp")
    public String signUp(Model model){
        model.addAttribute(new SignUpDTO());
        return "signUp";
    }
    @PostMapping("/signUp")
    public String signUp(Model model, @ModelAttribute @Valid SignUpDTO signUpDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "signUp";
        }
        try{
            Member member = memberService.saveMember(signUpDTO);
        }catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
            return "signUp";
        }

        return "redirect:/login";
    }


    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute(new LoginDTO());
        return "login";
    }


}
