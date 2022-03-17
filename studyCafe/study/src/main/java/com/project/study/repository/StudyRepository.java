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

    List<Study> findByMembers(Account account);

    @EntityGraph(attributePaths = {"tags","managers"})
    Study findStudyWithTagsAndManagersByPath(String path);

    @EntityGraph(attributePaths = {"zones","managers"})
    Study findStudyWithZonesAndManagersByPath(String path);

    @EntityGraph(attributePaths = {"managers"})
    Study findStudyWithManagersByPath(String path);

    @EntityGraph(attributePaths = {"members"})
    Study findStudyWithMembersByPath(String path);

    @EntityGraph(attributePaths = {"members","managers"})
    Study findStudyWithMembersAndManagersByPath(String path);

}
