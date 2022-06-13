package com.auth.test.repository;

import com.auth.test.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {

    Account findByNickname(String username);
}
