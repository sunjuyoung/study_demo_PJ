package com.project.study.service;

import com.project.study.domain.Account;
import com.project.study.domain.Study;
import com.project.study.dto.StudyDescriptionForm;
import com.project.study.dto.StudyForm;
import com.project.study.repository.AccountRepository;
import com.project.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;

    public String createStudy(Account account, StudyForm studyForm) {
        Study study = modelMapper.map(studyForm, Study.class);
        Study newStudy = studyRepository.save(study);
        newStudy.addManager(account);
        newStudy.addMember(account);
        return newStudy.getPath();
    }

    public Study getStudyByPath(String path) {
        Study study = studyRepository.findByPath(path).orElseThrow();
        return study;
    }

    public Study updateStudyDescription(StudyDescriptionForm studyDescriptionForm, String path) {
        Study study = studyRepository.findByPath(path).orElseThrow();
        modelMapper.map(studyDescriptionForm,study);
        Study saveStudy = studyRepository.save(study);
        return saveStudy;
    }
}
