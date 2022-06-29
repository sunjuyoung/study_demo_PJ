package com.example.security.config;

import com.example.security.auth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)//secured, prepost 어노테이션활성화,
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록됩니다.
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/user/**").authenticated();
        http.authorizeRequests().antMatchers("/manager/**").access("hasAnyAuthority('ROLE_MANAGER','ROLE_ADMIN')");
        http.authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
        http.formLogin().loginPage("/login").defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/login")
        //구글 로그인 후처리 1.코드받기(인증) 2. 엑세스토큰(권한) 3. 사용자 정보가져오기
        //4.자동 회원가입 or 추가 정보입력 후 회원가입
                .userInfoEndpoint()
                .userService(principalOauth2UserService);


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }


}
