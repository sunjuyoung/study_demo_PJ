package com.example.security.auth;


import com.example.security.model.Account;
import com.example.security.repository.AccountRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

   private final BCryptPasswordEncoder bCryptPasswordEncoder;
   private final AccountRespository accountRespository;

    //구글로 부터 받은 userrequest 데이터에 대한 후처리
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("getAdditionalParameters  ===== {}" ,userRequest.getAdditionalParameters());
        log.info("getAccessToken  ===== {}" ,userRequest.getAccessToken());
        log.info("name??  ===== {}" ,userRequest.getClientRegistration().getClientName());
        log.info("name??  ===== {}" ,userRequest.getClientRegistration().getClientId());
        //구글 로그인 완료 - 코드 리턴 - 엑세스 토큰요청 -> userrequest 정보 (loadUser)
        log.info("getAttributes??  ===== {}" ,super.loadUser(userRequest).getAttributes());


        //자동 회원가입
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getClientId();//google
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String username = provider+"_"+providerId; //google_340953490
        String password = bCryptPasswordEncoder.encode("더미패스워드");//필요없음 임의로
        String role = "ROLE_USER";

        Account account = new Account();
        if(!accountRespository.existsByUsername(username)){
             account = Account.builder()
                    .password(password)
                    .email(email)
                    .provider(provider)
                    .providerId(providerId)
                    .role(role)
                    .username(username)
                     .createdAt(LocalDateTime.now())
                    .build();
            accountRespository.save(account);
        }

        //return super.loadUser(userRequest);
        return new GoogleAccount(account,oAuth2User);
    }
}
