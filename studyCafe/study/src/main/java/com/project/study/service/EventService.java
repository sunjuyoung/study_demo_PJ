package com.project.study.service;

import com.project.study.domain.Account;
import com.project.study.domain.Enrollment;
import com.project.study.domain.Event;
import com.project.study.domain.Study;
import com.project.study.dto.EventForm;
import com.project.study.repository.EnrollmentRepository;
import com.project.study.repository.EventRepository;
import com.project.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final StudyRepository studyRepository;
    private final ModelMapper modelMapper;
    private final EnrollmentRepository enrollmentRepository;

    public Event createEvent(Study study, EventForm eventForm, Account account){
        Event event = modelMapper.map(eventForm, Event.class);
        event.setCreateBy(account);
        event.setStudy(study);
        event.setCreatedDateTime(LocalDateTime.now());
        Enrollment enrollment = new Enrollment();
        enrollment.newEvent(account,event);
        enrollmentRepository.save(enrollment);
        event.getEnrollments().add(enrollment);
        return eventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public Event getEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow();
        return event;
    }

    @Transactional(readOnly = true)
    public List<Event>  findEventsByStudy(Study study) {
        List<Event> events = eventRepository.findEventWithEnrollmentsByStudy(study);
        return events;
    }

    public Event updateEvent(EventForm eventForm, Long id) {
        Event event = getEvent(id);
        event.setTitle(eventForm.getTitle());
        event.setDescription(eventForm.getDescription());
        event.setEndEnrollmentDateTime(eventForm.getEndEnrollmentDateTime());
        event.setStartDateTime(eventForm.getStartDateTime());
        return event;
    }


    public void deleteEvent(Long id) {
        Event event = getEvent(id);
        eventRepository.delete(event);
    }
}
