package com.example.springsecurity.service;

import com.example.springsecurity.entity.PasswordResetToken;
import com.example.springsecurity.entity.User;
import com.example.springsecurity.entity.VerificationToken;
import com.example.springsecurity.model.PasswordModel;
import com.example.springsecurity.model.UserModel;
import com.example.springsecurity.repository.PasswordTokenRepository;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordTokenRepository passwordTokenRepository;

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

    @Override
    public VerificationToken generateNewVerifyToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user,token);
        passwordTokenRepository.save(passwordResetToken);
    }

    @Transactional
    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken =  passwordTokenRepository.findByToken(token);
        if(passwordResetToken == null){
            log.info("inValid null");
            return "inValid";
        }
        User user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if((passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0 ){
            passwordTokenRepository.delete(passwordResetToken);
            log.info("expired");
            return "expired";
        }

        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token, PasswordModel passwordModel) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword,user.getPassword());
    }
}
