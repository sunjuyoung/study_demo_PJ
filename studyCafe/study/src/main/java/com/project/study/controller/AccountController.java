package com.project.study.controller;

import com.project.study.auth.CurrentUser;
import com.project.study.valid.SignUpFormValidation;
import com.project.study.domain.Account;
import com.project.study.dto.SignUpForm;
import com.project.study.repository.AccountRepository;
import com.project.study.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
        accountService.login(newAccount);
        return "redirect:/";
    }

    @GetMapping("/check-email")
    public String checkEmail(@CurrentUser Account account, Model model){
        model.addAttribute(account);
       // accountService.sendSignUpConfirmEmail(account);

        return "account/check-email";
    }

    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(@CurrentUser Account account,Model model){
        if(!account.checkSendConfirmEmail()){
            model.addAttribute("error","잠시 뒤 다시 전송해 주세요");
            model.addAttribute(account);
            return "account/check-email";
        }

        model.addAttribute(account);
        accountService.sendSignUpConfirmEmail(account);

        return "redirect:/";

    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token,String email,Model model){
        Account account = accountService.checkEmailToken(email);
        if(account==null){
             model.addAttribute("error","wrong.email");
            return "account/checked-email";
        }
        if(!account.getEmailCheckToken().equals(token)){
            model.addAttribute("error","wrong.token");
            return "account/checked-email";
        }
        model.addAttribute("nickname",account.getNickname());
        return "account/checked-email";
    }


    @GetMapping("/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname,Model model,@CurrentUser Account account){
        Account getAccount = accountService.getAccount(nickname);

        model.addAttribute("account",getAccount);
        model.addAttribute("isOwner",getAccount.equals(account));
        return "account/profile";
    }

    @GetMapping("/email-login")
    public String emailLoginForm(@CurrentUser Account account,Model model){
        return "account/email-login";
    }

    @PostMapping("/email-login")
    public String submitEmailLogin(@ModelAttribute("email")String email, Model model, RedirectAttributes redirectAttributes){
        Account account = accountRepository.findByEmail(email);
        if(account == null){
            model.addAttribute("message","해당 이메일은 존재 하지 않습니다.");
            return "account/email-login";
        }

        if(!account.checkSendConfirmEmail()){
            model.addAttribute("error","반복");
            return "account/email-login";
        }
        accountService.sendLoginLink(account);
        return "redirect:/email-login";
    }

    @GetMapping("/login-by-email")
    public String loginByEmail(String token,String email,Model model){
        Account account = accountRepository.findByEmail(email);
        if(account==null){
            model.addAttribute("error","wrong.email");
            return "account/email-login";
        }
        if(!account.getEmailCheckToken().equals(token)){
            model.addAttribute("error","wrong.token");
            return "account/email-login";
        }
        accountService.login(account);

        return "account/login-by-email";

    }

}
