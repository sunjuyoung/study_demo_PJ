package com.project.study.controller;

import com.project.study.domain.Account;
import com.project.study.infra.AbstractContainerBaseTest;
import com.project.study.infra.MockMvcTest;
import com.project.study.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@MockMvcTest
class AccountControllerTest extends AbstractContainerBaseTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;


    @Test
    @DisplayName("인증 메일 확인 실패")
    public void test1() throws Exception{
        mockMvc.perform(get("/check-email-token")
        .param("email","test@naver.com")
        .param("token","sdf"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @DisplayName("인증 메일 확인")
    @Transactional
    public void checkMailFail() throws Exception{

        Account account = Account.builder()
                .nickname("test")
                .password("12341234")
                .email("test@naver.com")
                .build();
        Account newAccount = accountRepository.save(account);
        newAccount.generateToken();

        mockMvc.perform(get("/check-email-token")
                .param("email",newAccount.getEmail())
                .param("token",newAccount.getEmailCheckToken()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("nickname"))
        .andExpect(model().attributeDoesNotExist("error"));
    }
    

    @Test
    @DisplayName("회원 가입 폼")
    public void test() throws Exception{
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));
    }

    @Test
    @DisplayName("회원 가입")
    public void signUpForm() throws Exception{
        mockMvc.perform(post("/sign-up")
                        .param("nickname","sun")
                        .param("email","test@naver.com")
                        .param("password","12341234"))
                //.andExpect(view().name("account/sign-up"));  회원 가입 실패
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
        .andExpect(authenticated());

        assertTrue(accountRepository.existsByEmail("test@naver.com"));
        Account account = accountRepository.findByEmail("test@naver.com");
        assertNotNull(account);
        assertNotNull(account.getEmailCheckToken());
        assertNotEquals(account.getPassword(),"12341234");

    }
}