package com.project.study.repository;


import com.project.study.domain.Account;
import com.project.study.domain.Study;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly=true)
public interface StudyRepository extends JpaRepository<Study,Long>{

    boolean existsByPath(String path);

    @EntityGraph(value = "Study.withAll",type = EntityGraph.EntityGraphType.LOAD)
    Optional<Study> findByPath(String path);

    List<Study> findAllByMembers(Account account);


}
