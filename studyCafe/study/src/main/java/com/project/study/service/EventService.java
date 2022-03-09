package com.project.study.service;

import com.project.study.domain.Account;
import com.project.study.domain.Event;
import com.project.study.domain.Study;
import com.project.study.dto.EventForm;
import com.project.study.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public Event createEvent(Study study, EventForm eventForm, Account account){
        Event event = modelMapper.map(eventForm, Event.class);
        event.setCreateBy(account);
        event.setStudy(study);
        event.setCreatedDateTime(LocalDateTime.now());
        return eventRepository.save(event);


    }
}
