package com.project.study.controller;

import com.project.study.dto.SignUpForm;
import com.project.study.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {


    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountService accountService;

    @BeforeEach
    void beforeEach(){

    }
    @AfterEach
    void afterEach(){

    }

    @DisplayName("로그인")
    @Test
    void login_with_email()throws Exception{

        newAccount();


        mockMvc.perform(post("/login")
                .param("username","test")
                .param("password","12341234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated().withUsername("test"));
    }


    @DisplayName("로그인 실패")
    @Test
    void login_fail()throws Exception{

        newAccount();


        mockMvc.perform(post("/login")
                .param("username","test1")
                .param("password","12341234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/login?error"));
    }

    @DisplayName("로그아웃")
    @Test
    void logout()throws Exception{


        mockMvc.perform(post("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(unauthenticated())
                .andExpect(redirectedUrl("/"));
    }

    private void newAccount() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setEmail("test@naver.com");
        signUpForm.setNickname("test");
        signUpForm.setPassword("12341234");
        accountService.saveNewAccount(signUpForm);
    }

}