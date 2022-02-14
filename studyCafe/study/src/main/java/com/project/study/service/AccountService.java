package com.project.study.service;

import com.project.study.auth.UserAccount;
import com.project.study.config.AppProperties;
import com.project.study.domain.Account;
import com.project.study.domain.Tag;
import com.project.study.domain.Zone;
import com.project.study.dto.SignUpForm;
import com.project.study.dto.ZoneForm;
import com.project.study.mail.EmailMessage;
import com.project.study.mail.EmailService;
import com.project.study.repository.AccountRepository;
import com.project.study.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ZoneRepository zoneRepository;
    private final TemplateEngine templateEngine;

    private final AppProperties appProperties;

    public Account saveNewAccount(SignUpForm signUpForm) {
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = modelMapper.map(signUpForm, Account.class);
        account.generateToken();
        return accountRepository.save(account);
    }

    public void sendSignUpConfirmEmail(Account newAccount) {
        Context context = new Context();
        String link = "/check-email-token?token="+newAccount.getEmailCheckToken()+"&email="+newAccount.getEmail();
        context.setVariable("link",link);
        context.setVariable("nickname",newAccount.getNickname());
        context.setVariable("linkName","이메일 인증하기");
        context.setVariable("message","스터디 서비스를 사용하려면 링크를 클릭하세요");
        context.setVariable("host",appProperties.getHost());

        String message = templateEngine.process("mail/simple-link",context);
        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("스터디 회원 가입 인증")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
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
        Context context = new Context();
        context.setVariable("link","/check-email-token?token="+account.getEmailCheckToken()+"&email="+account.getEmail());
        context.setVariable("nickname",account.getNickname());
        context.setVariable("linkName","이메일 로그인하기");
        context.setVariable("message","로그인 하려면  링크를 클릭하세요");
        context.setVariable("host",appProperties.getHost());

        String message = templateEngine.process("mail/simple-link",context);

        account.generateToken();

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("로그인 링크")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
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

    public Set<Zone> getZones(Account account) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        return byId.orElseThrow().getZones();
    }

    public void addZone(Account account, ZoneForm zoneForm) {
        Optional<Zone> byCity = zoneRepository.findByCity(zoneForm.getCityName());
        Optional<Account> byId = accountRepository.findById(account.getId());
        byId.ifPresent(a->{
            a.getZones().add(byCity.orElseThrow());
        });
    }

    public void removeZone(Account account, ZoneForm zoneForm) {
        Optional<Account> byId = accountRepository.findById(account.getId());
        Optional<Zone> zone = zoneRepository.findByCity(zoneForm.getCityName());
        byId.ifPresent(a-> a.getZones().remove(zone.orElseThrow()));
    }

    public Account checkEmailToken(String email) {
        Account account = accountRepository.findByEmail(email);
        account.completeSignUp();
        login(account);
        return account;
    }
}
