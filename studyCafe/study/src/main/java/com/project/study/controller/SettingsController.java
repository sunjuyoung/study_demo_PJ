package com.project.study.controller;

import com.project.study.auth.CurrentUser;
import com.project.study.domain.Account;
import com.project.study.dto.ProfileForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SettingsController {

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute("profile",new ProfileForm(account));

        return "settings/profile";
    }

    @PostMapping("/settings/profile")
    public String submitProfile(@CurrentUser Account account, Model model, @ModelAttribute("profile") ProfileForm profileForm){
        model.addAttribute(account);
        model.addAttribute("profile",new ProfileForm(account));

        return "settings/profile";
    }
}
