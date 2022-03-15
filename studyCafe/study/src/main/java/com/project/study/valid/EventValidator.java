package com.project.study.valid;

import com.project.study.domain.Event;
import com.project.study.dto.EventForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class EventValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EventForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EventForm eventForm = (EventForm)target;
        if(eventForm.getEndEnrollmentDateTime().isBefore(LocalDateTime.now())){
            errors.rejectValue("endEnrollmentDateTime","wrong.datetime","모임 접수 종료 일시를 정확히 입력해주세요.");
        }
        if(eventForm.getEndDateTime().isBefore(eventForm.getStartDateTime()) ||
                eventForm.getEndDateTime().isBefore(eventForm.getEndEnrollmentDateTime())){
            errors.rejectValue("endEnrollmentDateTime","wrong.datetime","모임 종료 일시를 정확히 입력해주세요.");
        }
        if(eventForm.getStartDateTime().isBefore(eventForm.getEndEnrollmentDateTime())){
            errors.rejectValue("endEnrollmentDateTime","wrong.datetime","모임 접수 시작 일시를 정확히 입력해주세요.");
        }


    }


    public void validateUpdateForm(EventForm eventForm, Event event,Errors errors){
        if(eventForm.getLimitOfEnrollments() < event.getLimitOfEnrollment()){
            errors.rejectValue("limitOfEnrollment","wrong.value","기존 모집 인원 수 보다 커야 합니다.");
        }
    }
}
