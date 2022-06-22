package com.demo.api;

import com.demo.domain.Role;
import com.demo.domain.User;
import com.demo.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserResource {

    private final UserService userService;


    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok().body(allUsers);
    }

    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        User user1 = userService.saveUser(user);
        return ResponseEntity.created(uri).body(user1);
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        Role role1 = userService.saveRole(role);
        return ResponseEntity.ok().body(role1);
    }

    @PostMapping("/role/addToUser")
    public ResponseEntity<?> addRole(@RequestBody RoleToUserForm roleToUserForm){
         userService.addRoleToUser(roleToUserForm.getUsername(),roleToUserForm.getRoleName());
        return ResponseEntity.ok().build();
    }


}

@Data
class RoleToUserForm{
    private String username;
    private String roleName;
}
