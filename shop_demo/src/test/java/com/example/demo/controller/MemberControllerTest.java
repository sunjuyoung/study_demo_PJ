package com.example.demo.controller;

import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MockMvc mockMvc;


    @Test
    @Rollback(value = false)
    public void tes1t() throws Exception{
        //given
        Member byEmail = memberRepository.findByEmail("syseoz@naver.com");
        byEmail.changeRoleByAdmin();

        //when

        //then
    }

    @Test
    public void test() throws Exception{
        //given
        String email = "syseoz@naver.com";
        String password = "12341234";
        //when
        mockMvc.perform(formLogin().userParameter("email")
        .loginProcessingUrl("/members/login")
        .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());

        //then
    }



}