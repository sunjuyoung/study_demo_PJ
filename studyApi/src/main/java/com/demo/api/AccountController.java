package com.demo.api;

import com.demo.domain.Role;
import com.demo.dto.ResponseUser;
import com.demo.dto.RoleToUserForm;
import com.demo.dto.SignUpDTO;
import com.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @PostMapping("/user/save")
    public ResponseEntity<ResponseUser> saveUser(@RequestBody SignUpDTO signUpDTO){
        ResponseUser responseUser = accountService.saveUser(signUpDTO);
        return ResponseEntity.ok().body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getAllUsers(){
        List<ResponseUser> allUsers = accountService.findAllUsers();
        return ResponseEntity.ok().body(allUsers);
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        //URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        Role role1 = accountService.saveRole(role);
        return ResponseEntity.ok().body(role1);
    }

    @PostMapping("/role/addToUser")
    public ResponseEntity<?> addRole(@RequestBody RoleToUserForm roleToUserForm){
        accountService.addRoleToUser(roleToUserForm.getUsername(),roleToUserForm.getRoleName());
        return ResponseEntity.ok().build();
    }
}
