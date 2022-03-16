package com.project.study.service;

import com.project.study.domain.Account;
import com.project.study.domain.Study;
import com.project.study.domain.Tag;
import com.project.study.domain.Zone;
import com.project.study.dto.StudyDescriptionForm;
import com.project.study.dto.StudyForm;
import com.project.study.dto.TagForm;
import com.project.study.dto.ZoneForm;
import com.project.study.repository.AccountRepository;
import com.project.study.repository.StudyRepository;
import com.project.study.repository.TagRepository;
import com.project.study.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
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

    public void mangerCheck(Account account, Study study) {
        if (!account.isManager(study)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    public Study addZones(Account account, ZoneForm zoneForm, String path) {
        Study study = studyRepository.findStudyWithZonesAndManagersByPath(path);
        mangerCheck(account, study);
        Optional<Zone> byCity = zoneRepository.findByCity(zoneForm.getCityName());
        byCity.ifPresent(i->study.getZones().add(i));
        return study;
    }

    public Study removeTags(Account account, TagForm tagForm, String path) {
        Study studyByPath = studyRepository.findStudyWithTagsAndManagersByPath(path);
        mangerCheck(account,studyByPath);
        Tag tag = tagRepository.findByTitle(tagForm.getTagTitle()).orElseThrow();
        studyByPath.getTags().remove(tag);
        return studyByPath;
    }

    /**
     * 스터디 시작,종료
     */
    public Study publishStudy(Account account, String path) {
        Study studyByPath = getStudyUpdateStatus(account,path);
        studyByPath.studyPublish();
        return studyByPath;
    }
    public Study closeStudy(Account account, String path) {
        Study studyByPath = getStudyUpdateStatus(account,path);
        studyByPath.studyClose();
        return studyByPath;
    }

    /**
     * 스터디 모집
     */
    public Study updateStudyRecruitStatus(Account account, String path) {
        Study studyByPath = getStudyUpdateStatus(account,path);
        studyByPath.recruitStart();
        return studyByPath;
    }
    public Study stopStudyRecruitStatus(Account account, String path) {
        Study studyByPath = getStudyUpdateStatus(account,path);
        studyByPath.recruitStop();
        return studyByPath;
    }

    public Study getStudyTagByPath(String path) {
        Study study = getStudyByPath(path);
        return study;
    }

    public Study removeZones(Account account, ZoneForm zoneTitle, String path) {
        Study study = studyRepository.findStudyWithZonesAndManagersByPath(path);
        mangerCheck(account, study);
        Optional<Zone> byCity = zoneRepository.findByCity(zoneTitle.getCityName());
        byCity.ifPresent(i->study.getZones().remove(i));
        return study;
    }

    public void remove(Study studyUpdateStatus) {
        if(studyUpdateStatus.isRemovable()){
            studyRepository.delete(studyUpdateStatus);
        }else{
            throw new IllegalArgumentException("스터디를 삭제할 수 없습니다");
        }
    }

    public Study getStudyUpdateStatus(Account account, String path) {
        Study studyWithManagersByPath = studyRepository.findStudyWithManagersByPath(path);
        mangerCheck(account, studyWithManagersByPath);
        return studyWithManagersByPath;
    }

    public Study joinStudy(Account account, String path) {
        Study study = getStudyWithMember(path);
        if(!account.isMembers(study)){
            study.addMember(account);
        }
        return study;
    }

    public Study getStudyWithMember(String path) {
        return studyRepository.findStudyWithMembersByPath(path);
    }

    public Study leaveStudy(Account account, String path) {
        Study study = getStudyWithMember(path);
        if(account.isMembers(study) && !account.isManager(study)){
            study.removeMember(account);
        }
        return study;
    }
}
