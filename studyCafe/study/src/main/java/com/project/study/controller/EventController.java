package com.project.study.controller;

import com.project.study.auth.CurrentUser;
import com.project.study.domain.Account;
import com.project.study.domain.Event;
import com.project.study.domain.Study;
import com.project.study.dto.EventForm;
import com.project.study.service.EventService;
import com.project.study.service.StudyService;
import com.project.study.valid.EventValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
@RequestMapping("/study/{path}")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final StudyService studyService;
    private final EventValidator eventValidator;

    @InitBinder("eventForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(eventValidator);
    }

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
        Event event = eventService.createEvent(study, eventForm, account);
        model.addAttribute(event);
        model.addAttribute(study);


        return "redirect:/study/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/events/"+event.getId();
    }

}
