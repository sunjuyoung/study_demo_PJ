package com.project.study.repository;

import com.project.study.domain.Event;
import com.project.study.domain.Study;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface EventRepository extends JpaRepository<Event,Long> {

    List<Event> findByStudy(Study study);

    @EntityGraph(attributePaths = {"enrollments"})
    List<Event> findEventWithEnrollmentsByStudy(Study study);


}
