package com.demo.registration;

import com.demo.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RegistrationController {

    private final AppUserService appUserService;
    private final RegistrationService registrationService;

    @PostMapping("/registration")
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }


}
