package com.project.study.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.study.domain.Account;
import com.project.study.domain.Tag;
import com.project.study.dto.SignUpForm;
import com.project.study.dto.TagForm;
import com.project.study.repository.AccountRepository;
import com.project.study.repository.TagRepository;
import com.project.study.service.AccountService;
import com.project.study.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
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

    @Autowired
    SettingsService settingsService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TagRepository tagRepository;


    @BeforeEach
     void newAccount() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setEmail("test@naver.com");
        signUpForm.setNickname("test");
        signUpForm.setPassword("12341234");
        accountService.saveNewAccount(signUpForm);
    }
    @WithUserDetails(value = "test",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("tag")
    @Test
    public void testTag() throws Exception{
        mockMvc.perform(get("/settings/tags"))
                .andExpect(view().name("settings/tags"))
                .andExpect(model().attributeExists("tags"))
                .andExpect(model().attributeExists("whitelist"));
    }
    @WithUserDetails(value = "test",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("tagAdd")
    @Test
    public void testTagAdd() throws Exception{

        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post("/settings/tags/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(tagForm)))
                .andExpect(status().isOk());

        Optional<Tag> newTag = tagRepository.findByTitle("newTag");
        assertNotNull(newTag.get());

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


    @WithUserDetails(value = "test",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("비밀번호 변경")
    @Test
    public void test1() throws Exception{

        String newPassword = "43214321";
        mockMvc.perform(post("/settings/password").param("newPassword",newPassword)
        .param("newPasswordConfirm",newPassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"));

        Account byNickname = accountRepository.findByNickname("test");
        assertTrue(passwordEncoder.matches(newPassword,byNickname.getPassword()));
    }


}