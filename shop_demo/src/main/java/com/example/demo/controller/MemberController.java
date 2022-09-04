package com.example.demo.controller;

import com.example.demo.dto.SignUpFormDto;
import com.example.demo.entity.Member;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/new")
    public String memberForm(Model model){
        model.addAttribute(new SignUpFormDto());
        return "member/signUpForm";
    }

    @PostMapping("/new")
    public String submitSignUpForm(@Valid SignUpFormDto signUpFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "member/signUpForm";
        }
        try {
            Member member = new Member(signUpFormDto.getEmail(),signUpFormDto.getPassword(),
                    signUpFormDto.getAddress(),passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){

        }
        return "redirect:/";
    }
}
