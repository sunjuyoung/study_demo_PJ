package com.example.springsecurity.entity.listener;

import com.example.springsecurity.entity.User;
import com.example.springsecurity.event.RegistrationCompleteEvent;
import com.example.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //create vertification token
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token,user);


        //send email to user
        String url = event.getApplicationUrl() + "/verifyRegistration?token="+ token;
        log.info("link to verify your account : {}" ,url);
    }
}
