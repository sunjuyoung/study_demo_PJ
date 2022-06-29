package com.example.security.controller;

import com.example.security.auth.CurrentUser;
import com.example.security.auth.GoogleAccount;
import com.example.security.auth.PrincipalDetails;
import com.example.security.model.Account;
import com.example.security.repository.AccountRespository;
import com.example.security.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.web.servlet.oauth2.client.OAuth2ClientSecurityMarker;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final AccountRespository accountRespository;
    private final AccountService accountService;

    @GetMapping("/test/login")//@AuthenticationPrincipal
    public @ResponseBody String testLogin(@CurrentUser Account account){
        log.info("/test/login============");
        log.info(account.getUsername());
        return "세션 정보 확인";

    }
    //oauth2 google login
    @GetMapping("/test/oauth/login") //authentication 에 OAuth2User들어온다
    public @ResponseBody String testOauthLogin(@AuthenticationPrincipal GoogleAccount googleAccount){
        log.info("/test/oauth/login============");
        log.info(googleAccount.getAttributes().toString());
        log.info(googleAccount.getAccount().getUsername());
        return "세션 정보 확인";
    }
    //oauth2 google login
    @GetMapping("/test/oauth/login2") //
    public @ResponseBody String testOauthLogin2(@CurrentUser Account account){
        log.info("/test/oauth/login22222============");
        log.info("/test/login============");
        log.info(account.getUsername());
        return "세션 정보 확인";
    }

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
