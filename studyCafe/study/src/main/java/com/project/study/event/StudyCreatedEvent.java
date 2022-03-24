package com.project.study.event;

import com.project.study.domain.Study;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StudyCreatedEvent{

    private Study study;

    public StudyCreatedEvent(Study study){
        this.study = study;
    }

}
