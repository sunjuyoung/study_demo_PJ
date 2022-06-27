package com.demo.service;

import com.demo.domain.Account;
import com.demo.domain.Role;
import com.demo.dto.ResponseUser;
import com.demo.dto.SignUpDTO;
import com.demo.mail.EmailMessage;
import com.demo.mail.EmailService;
import com.demo.repo.AccountRepository;
import com.demo.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import org.thymeleaf.TemplateEngine;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final Environment env;




    @Override
    public ResponseUser getUser(String nickname) {
        Account account = accountRepository.findByNickname(nickname);
        validUser(account);
        ResponseUser responseUser = modelMapper.map(account, ResponseUser.class);
        return responseUser;
    }


    @Override
    public List<ResponseUser> findAllUsers() {
        List<Account> accounts = accountRepository.findAll();
        log.info(String.valueOf(accounts.size()));
        List<ResponseUser> responseUsers = new ArrayList<>();
        accounts.stream().forEach(account -> responseUsers.add(modelMapper.map(account,ResponseUser.class)));
        return responseUsers;
    }

    @Override
    public ResponseUser saveUser(SignUpDTO signUpDTO) {
        Account account = modelMapper.map(signUpDTO, Account.class);
        if(accountRepository.existsByNickname(account.getNickname())){
            throw new IllegalStateException("user already exists");
        }
        account.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        account.generateToken();
        Account newUser = accountRepository.save(account);
       // sendSignUpConfirmEmail(newUser);
        return modelMapper.map(newUser,ResponseUser.class);
    }
    public void sendSignUpConfirmEmail(Account newAccount) {
        String link = "/check-email-token?token="+newAccount.getEmailCheckToken()+"&email="+newAccount.getEmail();
        Context context = new Context();
        context.setVariable("link",link);
        context.setVariable("nickname",newAccount.getNickname());
        context.setVariable("linkName","이메일 인증하기");
        context.setVariable("message","아래 링크를 클릭하여 이메일 인증을 완료하세요");
        context.setVariable("host",env.getProperty("app.host"));
        String message = templateEngine.process("mail/simple-link", context);


        EmailMessage emailMessage = EmailMessage.builder()
                .message(message)
                .subject("회원 가입 인증")
                .to(newAccount.getEmail())
                .build();
        emailService.sendEmail(emailMessage);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        Account account = accountRepository.findByNickname(username);
        validUser(account);
        Role role = roleRepository.findByName(roleName);
        account.getRoles().add(role);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void checkEmailToken(String token, String email) {
        Account account = accountRepository.findByEmail(email);
        validUser(account);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByNickname(username);
        validUser(account);
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        account.getRoles().forEach(role -> roles.add(new SimpleGrantedAuthority(role.getName())) );
        return new User(account.getNickname(),account.getPassword(),
                true,true,true,true,roles);
    }

    public void validUser(Account account) {
        if(account == null){
            throw new UsernameNotFoundException("user not found");
        }
    }
}
