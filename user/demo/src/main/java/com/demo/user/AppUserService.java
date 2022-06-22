package com.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,username)));
    }

    public String signUpUser(AppUser appUser){
        boolean userExist = appUserRepository.findByEmail(appUser.getEmail())
                .isPresent();
        if(userExist){
            throw new IllegalStateException("email already exists");
        }
        String encodePassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodePassword);
        appUserRepository.save(appUser);

        return "works signup";
    }
}
