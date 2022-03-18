package com.project.study.controller;

import com.project.study.auth.CurrentUser;
import com.project.study.domain.Account;
import com.project.study.domain.Event;
import com.project.study.domain.Study;
import com.project.study.dto.EventForm;
import com.project.study.repository.StudyRepository;
import com.project.study.service.EventService;
import com.project.study.service.StudyService;
import com.project.study.valid.EventValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/study/{path}")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final StudyService studyService;
    private final EventValidator eventValidator;
    private final ModelMapper modelMapper;
    private final StudyRepository studyRepository;

    @InitBinder("eventForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(eventValidator);
    }

    @GetMapping("/new-event")
    public String eventForm(@CurrentUser Account account, @PathVariable String path, Model model){
        Study study = studyRepository.findStudyWithMembersAndManagersByPath(path);
        model.addAttribute(study);
        model.addAttribute(account);
        model.addAttribute(new EventForm());

        return "event/form";
    }
    @PostMapping("/new-event")
    public String submitEvent(@CurrentUser Account account, @PathVariable String path, Model model,
                              @Valid @ModelAttribute EventForm eventForm , Errors errors){
        Study study = studyRepository.findStudyWithMembersAndManagersByPath(path);
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

    @GetMapping("/events/{id}")
    public String getEvent(@CurrentUser Account account,@PathVariable String path, @PathVariable Long id,Model model){
        Study study = studyRepository.findByPath(path).orElseThrow();
        Event event = eventService.getEvent(id);
        model.addAttribute(event);
        model.addAttribute(study);
        model.addAttribute(account);
        return "event/view";
    }

    @GetMapping("/events")
    public String getStudyEvents(@CurrentUser Account account,@PathVariable String path,Model model){
        Study study = studyRepository.findByPath(path).orElseThrow();
        List<Event> eventsByStudy = eventService.findEventsByStudy(study);
        List<Event> oldEvent = new ArrayList<>();
        List<Event> newEvent = new ArrayList<>();

        eventsByStudy.forEach(i->{
            if(i.getEndDateTime().isBefore(LocalDateTime.now())){
                oldEvent.add(i);
            }else{
                newEvent.add(i);
            }
        });
        model.addAttribute(account);
        model.addAttribute(study);
        model.addAttribute("oldEvents",oldEvent);
        model.addAttribute("newEvents",newEvent);
        return "study/events";
    }

    @GetMapping("/events/{id}/edit")
    public String updateEventForm(@CurrentUser Account account, @PathVariable String path, @PathVariable Long id,Model model){
        Study study = studyService.getStudyByPath(path);
        Event event = eventService.getEvent(id);

        EventForm eventForm = modelMapper.map(event, EventForm.class);

        model.addAttribute(eventForm);
        model.addAttribute(event);
        model.addAttribute(account);
        model.addAttribute(study);
        return "event/update-form";
    }

    @PostMapping("/events/{id}/edit")
    public String updateEvent(@CurrentUser Account account, @PathVariable String path, @PathVariable Long id,Model model,
                              @Valid @ModelAttribute EventForm eventForm, Errors errors){
        Study study = studyService.getStudyByPath(path);
        Event event = eventService.updateEvent(eventForm, id);
        eventValidator.validateUpdateForm(eventForm,event,errors);
        if(errors.hasErrors()){
            model.addAttribute(event);
            model.addAttribute(account);
            model.addAttribute(study);
            return "event/update-form";
        }

        model.addAttribute(event);
        model.addAttribute(account);
        model.addAttribute(study);
        return "redirect:/study/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/events/"+event.getId();
    }

    @DeleteMapping("/events/{id}")
    public String cancelEvent(@CurrentUser Account account,@PathVariable String path,@PathVariable Long id){
        Study study = studyService.getStudyByPath(path);
        studyService.mangerCheck(account,study);
        eventService.deleteEvent(id);
        return "redirect:/study/"+ URLEncoder.encode(path, StandardCharsets.UTF_8)+"/events/";
    }

    @PostMapping("/events/{id}/enroll")
    public String submitEnroll(@CurrentUser Account account, @PathVariable String path, @PathVariable Long id,Model model){
        Study study = studyService.getStudyByPath(path);
        Event event = eventService.getEvent(id);

        EventForm eventForm = modelMapper.map(event, EventForm.class);

        model.addAttribute(eventForm);
        model.addAttribute(event);
        model.addAttribute(account);
        model.addAttribute(study);
        return "event/update-form";
    }

}
