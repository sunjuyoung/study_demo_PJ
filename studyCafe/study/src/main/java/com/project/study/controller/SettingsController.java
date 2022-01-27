package com.project.study.controller;

import com.project.study.auth.CurrentUser;
import com.project.study.domain.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SettingsController {

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);

        return "";
    }
}
