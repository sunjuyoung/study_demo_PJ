package com.project.study.service;

import com.project.study.domain.Event;
import com.project.study.domain.Study;
import com.project.study.dto.EventForm;
import com.project.study.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public void createEvent(Study study, EventForm eventForm) {
        Event map = modelMapper.map(eventForm, Event.class);

    }
}
