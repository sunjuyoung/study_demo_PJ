package com.demo.repo;

import com.demo.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.*;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String roleName);
}
