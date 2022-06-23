package com.demo.repo;

import com.demo.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long> {

    Account findByNickname(String nickname);
    Boolean existsByNickname(String nickname);

}
