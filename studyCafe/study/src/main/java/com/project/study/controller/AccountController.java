package com.project.study.controller;

import com.project.study.config.SignUpFormValidation;
import com.project.study.domain.Account;
import com.project.study.dto.SignUpForm;
import com.project.study.repository.AccountRepository;
import com.project.study.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidation signUpFormValidation;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    //닉네임 이메일 중복 검증
    @InitBinder("singUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidation);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model){

        model.addAttribute("signUpForm",new SignUpForm());
        return "account/sign-up";
    }


    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid @ModelAttribute SignUpForm signUpForm, Errors errors){
        if(errors.hasErrors()){
            return "account/sign-up";
        }
        Account newAccount = accountService.saveNewAccount(signUpForm);

        accountService.sendSignUpConfirmEmail(newAccount);
        return "redirect:/";
    }


    @GetMapping("/check-email-token")
    public String checkEmailToken(String token,String email,Model model){
        Account account = accountRepository.findByEmail(email);
        if(account==null){
             model.addAttribute("error","wrong.email");
            return "account/checked-email";
        }
        if(!account.getEmailCheckToken().equals(token)){
            model.addAttribute("error","wrong.token");
            return "account/checked-email";
        }
        account.setEmailVerified(true);
        account.setJoinedAt(LocalDateTime.now());
        model.addAttribute("nickname",account.getNickname());
        return "account/checked-email";
    }


}
