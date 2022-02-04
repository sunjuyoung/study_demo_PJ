package com.project.study.valid;

import com.project.study.dto.NicknameForm;
import com.project.study.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class NicknameValidation implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return NicknameForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NicknameForm nicknameForm = (NicknameForm)target;
       if(accountRepository.existsByNickname(nicknameForm.getNickname())){
            errors.rejectValue("nickname","wrong.value","해당 닉네임은 이미 존재합니다.");
        }

    }
}
