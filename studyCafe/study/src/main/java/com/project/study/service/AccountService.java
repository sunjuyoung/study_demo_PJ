package com.project.study.service;

import com.project.study.auth.UserAccount;
import com.project.study.domain.Account;
import com.project.study.domain.Tag;
import com.project.study.dto.SignUpForm;
import com.project.study.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public Account saveNewAccount(SignUpForm signUpForm) {
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = modelMapper.map(signUpForm, Account.class);
/*        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .build();*/
        account.generateToken();
        return accountRepository.save(account);
    }

    public void sendSignUpConfirmEmail(Account newAccount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("스터디카페 가입인증");
        mailMessage.setText("/check-email-token?token="+newAccount.getEmailCheckToken()+"&email="+newAccount.getEmail());
        javaMailSender.send(mailMessage);
    }

    public void login(Account newAccount) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
               new UserAccount(newAccount),   //principle
                newAccount.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);
        if(account ==null){
            account =  accountRepository.findByNickname(emailOrNickname);
        }
        if(account == null){
            throw new UsernameNotFoundException(emailOrNickname);
        }

        return new UserAccount(account);
    }

    public void completeSignUp(Account account) {
        account.completeSignUp();//joinAt,EmailVerified
        login(account);
    }

    public Account getAccount(String nickname) {
        Account byNickname = accountRepository.findByNickname(nickname);
        if(byNickname==null){
            new IllegalArgumentException(byNickname +"에 해당하는 사용자가 없습니다");
        }
        return byNickname;

    }

    public void sendLoginLink(Account account) {
        account.generateToken();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(account.getEmail());
        mailMessage.setSubject("로그인 링크");
        mailMessage.setText("/login-by-email?token="+account.getEmailCheckToken()+"&email="+account.getEmail());
        javaMailSender.send(mailMessage);
    }


    public void addTag(Account account, Tag tag) {
        //detach 연간관계 toMany 관계는 null 값, lazy로딩 불가능
        Optional<Account> account1 = accountRepository.findById(account.getId());
        account1.ifPresent(a -> a.getTags().add(tag));

        //accountRepository.getOne() lazy로딩 이지만 detach라서 불가능
    }

    public Set<Tag> getTags(Account account) {
        Optional<Account> account1 = accountRepository.findById(account.getId());
        return account1.orElseThrow().getTags();
    }

    public void removeTag(Account account, Tag tag) {
        Optional<Account> account1 = accountRepository.findById(account.getId());
        account1.ifPresent(a -> a.getTags().remove(tag));
    }
}
