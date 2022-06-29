package com.example.security.auth;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    //구글로 부터 받은 userrequest 데이터에 대한 후처리
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("getAdditionalParameters  ===== {}" ,userRequest.getAdditionalParameters());
        log.info("getAccessToken  ===== {}" ,userRequest.getAccessToken());
        log.info("name??  ===== {}" ,userRequest.getClientRegistration().getClientName());
        log.info("name??  ===== {}" ,userRequest.getClientRegistration().getClientId());
        //구글 로그인 완료 - 코드 리턴 - 엑세스 토큰요청 -> userrequest 정보 (loadUser)
        log.info("getAttributes??  ===== {}" ,super.loadUser(userRequest).getAttributes());
        return super.loadUser(userRequest);
    }
}
