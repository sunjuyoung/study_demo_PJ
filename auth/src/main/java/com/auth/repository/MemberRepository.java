package com.auth.repository;

import com.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByNickname(String nickname);

    boolean existsByEmail(String email);
}
