package com.project.study.valid;

import com.project.study.dto.StudyForm;
import com.project.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class StudyFormValidation implements Validator {

    private final StudyRepository studyRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return StudyForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StudyForm studyForm = (StudyForm)target;
        if(studyRepository.existsByPath(studyForm.getPath())){
            errors.rejectValue("path","wrong.path","해당 스터디 경로는 이미 사용중 입니다.");
        }

    }
}
