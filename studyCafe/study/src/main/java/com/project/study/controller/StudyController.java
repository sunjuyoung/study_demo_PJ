package com.project.study.controller;

import com.project.study.auth.CurrentUser;
import com.project.study.domain.Account;
import com.project.study.dto.StudyDescriptionForm;
import com.project.study.dto.StudyForm;
import com.project.study.service.StudyService;
import com.project.study.valid.StudyFormValidation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Controller
@RequiredArgsConstructor
public class StudyController {

    private final StudyFormValidation studyFormValidation;
    private final StudyService studyService;
    private final ModelMapper modelMapper;

    @InitBinder("studyForm")
    public void InitBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(studyFormValidation);
    }


    @GetMapping("/new-study")
    public String studyCreateForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new StudyForm());
        return "study/createForm";
    }

    @PostMapping("/new-study")
    public String submitStudy(@CurrentUser Account account, Model model, @Valid @ModelAttribute StudyForm studyForm,
                              Errors errors, RedirectAttributes redirectAttributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return "study/createForm";
        }
        String newStudyPath = studyService.createStudy(account, studyForm);
        return "redirect:/study/"+URLEncoder.encode(newStudyPath, StandardCharsets.UTF_8);
    }

    @GetMapping("/study/{path}")
    public String studyForm(@CurrentUser Account account, Model model, @PathVariable String path){
        model.addAttribute("study", studyService.getStudyByPath(path));
        model.addAttribute(account);
        return "study/view";
    }

    @GetMapping("/study/{path}/members")
    public String studyMemberForm(@CurrentUser Account account, Model model, @PathVariable String path){
        model.addAttribute("study", studyService.getStudyByPath(path));
        model.addAttribute(account);
        return "study/members";
    }
    @GetMapping("/study/{path}/settings/description")
    public String studySettingDescription(@CurrentUser Account account, Model model, @PathVariable String path){
        model.addAttribute("studyDescriptionForm",new StudyDescriptionForm());
        model.addAttribute("study", studyService.getStudyByPath(path));
        model.addAttribute(account);
        return "study/settings/description";
    }
    @GetMapping("/study/{path}/settings/banner")
    public String studySettingBanner(@CurrentUser Account account, Model model, @PathVariable String path){
        model.addAttribute("study", studyService.getStudyByPath(path));
        model.addAttribute(account);
        return "study/members";
    }
    @GetMapping("/study/{path}/settings/tag")
    public String studySettingTag(@CurrentUser Account account, Model model, @PathVariable String path){
        model.addAttribute("study", studyService.getStudyByPath(path));
        model.addAttribute(account);
        return "study/members";
    }
    @GetMapping("/study/{path}/settings/zone")
    public String studySettingZone(@CurrentUser Account account, Model model, @PathVariable String path){
        model.addAttribute("study", studyService.getStudyByPath(path));
        model.addAttribute(account);
        return "study/members";
    }
}
