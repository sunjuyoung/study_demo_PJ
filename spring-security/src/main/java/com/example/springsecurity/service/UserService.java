package com.example.springsecurity.service;

import com.example.springsecurity.entity.User;
import com.example.springsecurity.entity.VerificationToken;
import com.example.springsecurity.model.PasswordModel;
import com.example.springsecurity.model.UserModel;

import java.util.Optional;

public interface UserService {
    User registerUser(UserModel userModel);

    void saveVerificationTokenForUser(String token, User user);

    String validateVerificationToken(String token);

    VerificationToken generateNewVerifyToken(String oldToken);

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token, PasswordModel passwordModel);

    void changePassword(User user, String newPassword);

    boolean checkValidOldPassword(User user, String oldPassword);
}
