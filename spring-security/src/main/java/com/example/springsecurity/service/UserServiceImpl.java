package com.example.springsecurity.service;

import com.example.springsecurity.entity.User;
import com.example.springsecurity.entity.VerificationToken;
import com.example.springsecurity.model.UserModel;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public User registerUser(UserModel userModel) {
        if(userRepository.existsByEmail(userModel.getEmail())){
            throw new RuntimeException("already exist user");
        }
        User user = modelMapper.map(userModel, User.class);
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        User saveUser = userRepository.save(user);

        return saveUser;
    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user,token);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken == null){
            return "inValid";
        }
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0 ){
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }
}
