package com.example.springsecurity.controller;

import com.example.springsecurity.entity.User;
import com.example.springsecurity.entity.VerificationToken;
import com.example.springsecurity.event.RegistrationCompleteEvent;
import com.example.springsecurity.model.PasswordModel;
import com.example.springsecurity.model.UserModel;
import com.example.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    private final ApplicationEventPublisher publisher;

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        String url = "";
        if(user != null){
            String token = UUID.randomUUID().toString();
             userService.createPasswordResetTokenForUser(user,token);
             url = passwordResetTokenMail(user,applicationUrl(request),token);
        }

        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel){
        String result = userService.validatePasswordResetToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "Invalid Token";
        }

        Optional<User> user = userService.getUserByPasswordResetToken(token,passwordModel);
        if(user.isPresent()){
            userService.changePassword(user.get(),passwordModel.getNewPassword());
            return "Password reset success";
        }else {
            return "Invalid Token";
        }

    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token="+ token;
        log.info(url);
        return url;
    }

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

    @GetMapping("/resendVerifyToken")
    public String resendVerifyToken(@RequestParam("token")String oldToken,
                                    HttpServletRequest request){
        VerificationToken verificationToken = userService.generateNewVerifyToken(oldToken);
        User user = verificationToken.getUser();
        resendVerifyTokenMail(user,applicationUrl(request),verificationToken);

        return "resend";
    }

    private void resendVerifyTokenMail(User user, String applicationUrl,VerificationToken verificationToken) {
        String url = applicationUrl + "/verifyRegistration?token="+ verificationToken.getToken();
        log.info(url);
    }


    private String applicationUrl(HttpServletRequest request) {
        return "http://"+
                request.getServerName()+
                ":"+
                request.getServerPort()+
                request.getContextPath();
    }
}
