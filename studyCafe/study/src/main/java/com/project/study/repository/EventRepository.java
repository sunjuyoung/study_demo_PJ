package com.project.study.repository;

import com.project.study.domain.Event;
import com.project.study.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    List<Event> findEventsByStudyOrderBysOrderByStartDateTime(Study study);
}
