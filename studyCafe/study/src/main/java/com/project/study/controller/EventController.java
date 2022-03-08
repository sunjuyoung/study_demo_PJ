package com.project.study.controller;

import com.project.study.auth.CurrentUser;
import com.project.study.domain.Account;
import com.project.study.domain.Study;
import com.project.study.dto.EventForm;
import com.project.study.service.EventService;
import com.project.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/study/{path}")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final StudyService studyService;

    @GetMapping("/new-event")
    public String eventForm(@CurrentUser Account account, @PathVariable String path, Model model){
        Study study = studyService.getStudyUpdateStatus(account, path);
        model.addAttribute(study);
        model.addAttribute(account);
        model.addAttribute(new EventForm());

        return "event/form";
    }
    @PostMapping("/new-event")
    public String submitEvent(@CurrentUser Account account, @PathVariable String path, Model model,
                              @Valid @ModelAttribute EventForm eventForm , Errors errors){
        Study study = studyService.getStudyUpdateStatus(account, path);
        if(errors.hasErrors()){
            model.addAttribute(study);
            model.addAttribute(account);
            return "event/form";
        }
        eventService.createEvent(study,eventForm);

        return "event/form";
    }

}