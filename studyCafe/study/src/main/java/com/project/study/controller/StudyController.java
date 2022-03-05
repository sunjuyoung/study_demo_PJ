package com.project.study.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.study.auth.CurrentUser;
import com.project.study.domain.Account;
import com.project.study.domain.Study;
import com.project.study.domain.Tag;
import com.project.study.domain.Zone;
import com.project.study.dto.StudyDescriptionForm;
import com.project.study.dto.StudyForm;
import com.project.study.dto.TagForm;
import com.project.study.dto.ZoneForm;
import com.project.study.repository.TagRepository;
import com.project.study.repository.ZoneRepository;
import com.project.study.service.StudyService;
import com.project.study.service.ZoneService;
import com.project.study.valid.StudyFormValidation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class StudyController {

    private final StudyFormValidation studyFormValidation;
    private final StudyService studyService;
    private final TagRepository tagRepository;
    private final ZoneRepository zoneRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;


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

    /**
     * description
     */
    @GetMapping("/study/{path}/settings/description")
    public String studySettingDescription(@CurrentUser Account account, Model model, @PathVariable String path){
        Study studyByPath = studyService.getStudyByPath(path);
        model.addAttribute("studyDescriptionForm",modelMapper.map(studyByPath,StudyDescriptionForm.class));
        model.addAttribute("study", studyByPath);
        model.addAttribute(account);
        return "study/settings/description";
    }

    @PostMapping("/study/{path}/settings/description")
    public String UpdateStudyDescription(@CurrentUser Account account,@Valid @ModelAttribute StudyDescriptionForm studyDescriptionForm,
                                         Errors errors,Model model, @PathVariable String path, RedirectAttributes redirectAttributes){
        Study studyByPath = studyService.getStudyByPath(path);
        if(errors.hasErrors()){
            model.addAttribute("study", studyByPath);
            model.addAttribute(account);
            return "study/settings/description";
        }

        Study study = studyService.updateStudyDescription(studyByPath,account,studyDescriptionForm);
        redirectAttributes.addFlashAttribute("message","스터디 소개가 수정 되었습니다.");
        redirectAttributes.addAttribute("study",study);
        return "redirect:/study/"+URLEncoder.encode(path, StandardCharsets.UTF_8);
    }
    /**
     * banner
     */
    @GetMapping("/study/{path}/settings/banner")
    public String studySettingBanner(@CurrentUser Account account, Model model, @PathVariable String path){
        model.addAttribute("study", studyService.getStudyByPath(path));
        model.addAttribute(account);
        return "study/settings/banner";
    }

    /**
     * tags
     */
    @GetMapping("/study/{path}/settings/tags")
    public String studySettingTag(@CurrentUser Account account, Model model, @PathVariable String path) throws JsonProcessingException {
        Study studyByPath = studyService.getStudyTagByPath(path);
        List<String> tagList =  tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("study",studyByPath);
        model.addAttribute("tags",studyByPath.getTags().stream().map(Tag::getTitle).collect(Collectors.toList()));
        model.addAttribute("whitelist",objectMapper.writeValueAsString(tagList));
        model.addAttribute(account);
        return "study/settings/tags";
    }

    @PostMapping("/study/{path}/settings/tags/add")
    @ResponseBody
    public ResponseEntity updateStudyTag(@CurrentUser Account account, Model model, @PathVariable String path,
                                         @RequestBody TagForm tagTitle){
        Study study = studyService.addTags(account, tagTitle, path);
        model.addAttribute("study", study);
        model.addAttribute(account);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/study/{path}/settings/tags/remove")
    @ResponseBody
    public ResponseEntity removeStudyTag(@CurrentUser Account account, Model model, @PathVariable String path,
                                         @RequestBody TagForm tagTitle){
        Study study = studyService.removeTags(account, tagTitle, path);
        model.addAttribute("study", study);
        model.addAttribute(account);
        return ResponseEntity.ok().build();
    }

    /**
     * zones
     */
    @GetMapping("/study/{path}/settings/zones")
    public String studySettingZone(@CurrentUser Account account, Model model, @PathVariable String path) throws JsonProcessingException {
        List<String> zoneList = zoneRepository.findAll().stream().map(Zone::toString).collect(Collectors.toList());
        Study studyByPath = studyService.getStudyByPath(path);
        model.addAttribute("study",studyByPath );
        model.addAttribute("whitelist",objectMapper.writeValueAsString(zoneList));
        model.addAttribute("zones",studyByPath.getZones().stream().map(Zone::toString).collect(Collectors.toList()));
        model.addAttribute(account);
        return "study/settings/zones";
    }

    @PostMapping("/study/{path}/settings/zones/add")
    @ResponseBody
    public ResponseEntity updateStudyZone(@CurrentUser Account account, Model model, @PathVariable String path,
                                          @RequestBody ZoneForm zoneName){
        Study study = studyService.addZones(account, zoneName, path);
        model.addAttribute("study", study);
        model.addAttribute(account);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/study/{path}/settings/zones/remove")
    @ResponseBody
    public ResponseEntity removeStudyZone(@CurrentUser Account account, Model model, @PathVariable String path,
                                          @RequestBody ZoneForm zoneTitle){
        Study study = studyService.removeZones(account, zoneTitle, path);
        model.addAttribute("study", study);
        model.addAttribute(account);
        return ResponseEntity.ok().build();
    }

    /**
     * Study
     */
    @GetMapping("/study/{path}/settings/study")
    public String studySettingStudy(@CurrentUser Account account, Model model, @PathVariable String path) {
        model.addAttribute("study", studyService.getStudyByPath(path));
        model.addAttribute(account);
        return "study/settings/study";
    }

    @PostMapping("/study/{path}/settings/study/publish")
    public String publishStudy(@CurrentUser Account account, Model model, @PathVariable String path) {
        Study study = studyService.publishStudy(account, path);
        model.addAttribute("study", study);
        model.addAttribute(account);
        return "study/settings/study";
    }
    @PostMapping("/study/{path}/settings/study/close")
    public String closeStudy(@CurrentUser Account account, Model model, @PathVariable String path) {
        Study study = studyService.closeStudy(account, path);
        model.addAttribute("study", study);
        model.addAttribute(account);
        return "study/settings/study";
    }

    @PostMapping("/study/{path}/settings/recruit/start")
    public String recruitStart(@CurrentUser Account account, Model model, @PathVariable String path) {
        Study study = studyService.updateStudyRecruitStatus(account, path);
        model.addAttribute("study", study);
        model.addAttribute(account);
        return "study/settings/study";
    }
    @PostMapping("/study/{path}/settings/recruit/stop")
    public String recruitStop(@CurrentUser Account account, Model model, @PathVariable String path) {
        Study study = studyService.stopStudyRecruitStatus(account, path);
        model.addAttribute("study", study);
        model.addAttribute(account);
        return "study/settings/study";
    }

    @PostMapping("/study/{path}/settings/study/remove")
    public String removeStudy(@CurrentUser Account account, @PathVariable String path,Model model){
        Study studyUpdateStatus = studyService.getStudyUpdateStatus(account, path);
            studyService.remove(studyUpdateStatus);
            return "redirect:/";
    }

    @GetMapping("/study/{path}/join")
    public String joinStudy(@CurrentUser Account account, @PathVariable String path,Model model){
         Study study = studyService.joinStudy(account,path);
        return "redirect:/study/"+URLEncoder.encode(path, StandardCharsets.UTF_8)+"/members";

    }
    @GetMapping("/study/{path}/leave")
    public String leaveStudy(@CurrentUser Account account, @PathVariable String path,Model model){
        Study study = studyService.leaveStudy(account,path);
        return "redirect:/study/"+URLEncoder.encode(path, StandardCharsets.UTF_8);
    }

}
