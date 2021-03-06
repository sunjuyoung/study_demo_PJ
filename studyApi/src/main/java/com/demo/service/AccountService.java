package com.demo.service;


import com.demo.domain.Account;
import com.demo.domain.Role;
import com.demo.dto.ResponseUser;
import com.demo.dto.SignUpDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountService extends UserDetailsService {

    ResponseUser getUser(String nickname);
    List<ResponseUser> findAllUsers();
    ResponseUser saveUser(SignUpDTO signUpDTO);


    void addRoleToUser(String username, String roleName);

    Role saveRole(Role role);

    void checkEmailToken(String token, String email);
}
