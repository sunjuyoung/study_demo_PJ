package com.project.study.service;

import com.project.study.domain.Account;
import com.project.study.domain.Study;
import com.project.study.domain.Tag;
import com.project.study.dto.StudyDescriptionForm;
import com.project.study.dto.StudyForm;
import com.project.study.dto.TagForm;
import com.project.study.dto.ZoneForm;
import com.project.study.repository.AccountRepository;
import com.project.study.repository.StudyRepository;
import com.project.study.repository.TagRepository;
import com.project.study.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
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
    private final TagRepository tagRepository;
    private final ZoneRepository zoneRepository;

    public String createStudy(Account account, StudyForm studyForm) {
        Study study = modelMapper.map(studyForm, Study.class);
        Study newStudy = studyRepository.save(study);
        newStudy.addManager(account);
        newStudy.addMember(account);
        return newStudy.getPath();
    }

    public Study getStudyByPath(String path) {
        Study study = studyRepository.findByPath(path).orElseThrow();
        if(study == null){
            throw new IllegalArgumentException(path + "에 해당하는 스터디가 없습니다.");
        }
        return study;
    }

    public Study updateStudyDescription(Study study, Account account,StudyDescriptionForm studyDescriptionForm) {
        mangerCheck(account, study);
        modelMapper.map(studyDescriptionForm,study);
        Study saveStudy = studyRepository.save(study);
        return saveStudy;
    }

    public Study addTags(Account account, TagForm tagForm, String path) {
        Study study = getStudyByPath(path);
        mangerCheck(account, study);
        Tag tag = tagRepository.findByTitle(tagForm.getTagTitle()).orElseGet(()->
           tagRepository.save(Tag.builder().title(tagForm.getTagTitle()).build()));
        study.getTags().add(tag);
        return study;
    }

    private void mangerCheck(Account account, Study study) {
        if (!account.isManager(study)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    public Study addZones(Account account, ZoneForm zoneForm, String path) {
        Study study = getStudyByPath(path);
        mangerCheck(account, study);
        return study;
    }
}
