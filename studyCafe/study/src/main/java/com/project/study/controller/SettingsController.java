package com.project.study.controller;

import com.project.study.auth.CurrentUser;
import com.project.study.domain.Account;
import com.project.study.dto.NicknameForm;
import com.project.study.dto.NotificationsForm;
import com.project.study.dto.PasswordForm;
import com.project.study.dto.ProfileForm;
import com.project.study.service.AccountService;
import com.project.study.service.SettingsService;
import com.project.study.valid.NicknameValidation;
import com.project.study.valid.PasswordFormValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SettingsController {

    private final AccountService accountService;
    private final SettingsService settingsService;
    private final PasswordFormValidation passwordFormValidation;
    private final NicknameValidation nicknameValidation;
    private final ModelMapper modelMapper;


    @InitBinder("passwordForm")
    public void initBinderPassword(WebDataBinder webDataBinder){
        webDataBinder.addValidators(passwordFormValidation);
    }

    @InitBinder("nicknameForm")
    public void initBinderNickname(WebDataBinder webDataBinder){
        webDataBinder.addValidators(nicknameValidation);
    }

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute("profile",new ProfileForm(account));

        return "settings/profile";
    }

    @PostMapping("/settings/profile")
    public String submitProfile(@CurrentUser Account account, Model model, @Valid @ModelAttribute("profile") ProfileForm profileForm,
                                Errors errors, RedirectAttributes redirectAttributes){
        if(errors.hasErrors()){
            model.addAttribute(account); //valid error 발생시 해당 폼 객체와 에러는
            return "settings/profile";
        }
        settingsService.updateProfile(account,profileForm);
        redirectAttributes.addFlashAttribute("message","프로필을 수정했습니다.");
        return "redirect:/profile/"+account.getNickname();
    }

    @GetMapping("/settings/password")
    public String profilePassword(@CurrentUser Account account,Model model){
        model.addAttribute(new PasswordForm());
        model.addAttribute(account);

        return "settings/password";
    }
    @PostMapping("/settings/password")
    public String changePassword(@CurrentUser Account account,Model model,@Valid @ModelAttribute PasswordForm passwordForm,
                                 Errors errors,RedirectAttributes redirectAttributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/password";
        }
        settingsService.changePassword(account,passwordForm);
        redirectAttributes.addFlashAttribute("message","비밀번호가 변경 되었습니다.");
        return "redirect:/profile/"+account.getNickname();
    }

    @GetMapping("/settings/notifications")
    public String notificationsForm(@CurrentUser Account account,Model model){
        NotificationsForm notificationsForm = modelMapper.map(account, NotificationsForm.class);
        model.addAttribute(notificationsForm);
        model.addAttribute(account);

        return "settings/notifications";
    }
    @PostMapping("/settings/notifications")
    public String updateNotifications(@CurrentUser Account account,Model model,@ModelAttribute NotificationsForm notificationsForm,
                                      RedirectAttributes redirectAttributes){
        settingsService.updateNotifications(account,notificationsForm);
        redirectAttributes.addFlashAttribute("message","알림 설정이 변경 되었습니다.");
        return "redirect:/profile/"+account.getNickname();
    }

    @GetMapping("/settings/account")
    public String nicknameForm(@CurrentUser Account account,Model model){
        model.addAttribute(new NicknameForm());
        model.addAttribute(account);

        return "settings/account";
    }

    @PostMapping("/settings/account")
    public String updateNickname(@CurrentUser Account account,Model model,RedirectAttributes redirectAttributes,
                                 @Valid @ModelAttribute NicknameForm nicknameForm,Errors errors){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/account";
        }
        settingsService.updateNickname(account,nicknameForm);
        redirectAttributes.addFlashAttribute("message","닉네임이 변경 되었습니다.");
        return "redirect:/profile/"+account.getNickname();
    }

    @GetMapping("/settings/tags")
    public String tagsForm(@CurrentUser Account account,Model model){


        return "settings/tags";
    }
}
