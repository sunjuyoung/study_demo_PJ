package com.project.study.repository;


import com.project.study.domain.Account;
import com.project.study.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study,Long>{

    boolean existsByPath(String path);

    Optional<Study> findByPath(String path);

    List<Study> findAllByMembers(Account account);
}
