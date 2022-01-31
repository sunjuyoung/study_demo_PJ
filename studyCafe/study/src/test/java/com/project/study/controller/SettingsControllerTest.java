package com.project.study.controller;

import com.project.study.domain.Account;
import com.project.study.dto.SignUpForm;
import com.project.study.repository.AccountRepository;
import com.project.study.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
     PasswordEncoder passwordEncoder;


    @BeforeEach
     void newAccount() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setEmail("test@naver.com");
        signUpForm.setNickname("test");
        signUpForm.setPassword("12341234");
        accountService.saveNewAccount(signUpForm);
    }

    @WithUserDetails(value = "test",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("프로필 수정하기")
    @Test
    public void test() throws Exception{
        String bio = "소개 수정 테스트";
        mockMvc.perform(post("/settings/profile")
                        .param("bio",bio))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"));

        Account test = accountRepository.findByNickname("test");
        assertEquals(test.getBio(),bio);
    }


/*    @WithUserDetails(value = "test",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("비밀번호 테스트")
    @Test
    public void test1() throws Exception{
        String password = "12341234";
        Account test = accountRepository.findByNickname("test");
        assertTrue(PasswordEncoderFactories.createDelegatingPasswordEncoder().matches(test.getPassword(),password));

    }*/


}