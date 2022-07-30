package com.example.springsecurity.controller;

import com.example.springsecurity.entity.User;
import com.example.springsecurity.event.RegistrationCompleteEvent;
import com.example.springsecurity.model.UserModel;
import com.example.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    private final ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        User user = userService.registerUser(userModel);
        publisher.publishEvent(new RegistrationCompleteEvent(
                user,
                applicationUrl(request)
        ));
        return "Success";
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token")String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "User verifies Success";
        }
        return "bad user";
    }


    private String applicationUrl(HttpServletRequest request) {
        return "http://"+
                request.getServerName()+
                ":"+
                request.getServerPort()+
                request.getContextPath();
    }
}
