package com.project.study.service;

import com.project.study.domain.Account;
import com.project.study.dto.NicknameForm;
import com.project.study.dto.NotificationsForm;
import com.project.study.dto.PasswordForm;
import com.project.study.dto.ProfileForm;
import com.project.study.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SettingsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public void updateProfile(Account account, ProfileForm profileForm) {
        account.setBio(profileForm.getBio());
        account.setLocation(profileForm.getLocation());
        account.setOccupation(profileForm.getOccupation());
        account.setUrl(profileForm.getUrl());
        account.setProfileImage(profileForm.getProfileImage());
        accountRepository.save(account);
    }

    public void changePassword(Account account, PasswordForm passwordForm) {
        account.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
        accountRepository.save(account);
    }

    public void updateNotifications(Account account, NotificationsForm notificationsForm) {
        modelMapper.map(notificationsForm,account);
        accountRepository.save(account);
    }

    public void updateNickname(Account account,NicknameForm nicknameForm) {
        account.setNickname(nicknameForm.getNickname());
        accountRepository.save(account);
    }
}
