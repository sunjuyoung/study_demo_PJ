package com.demo.security;

import com.demo.dto.RequestLogin;
import com.demo.dto.ResponseUser;
import com.demo.dto.SignUpDTO;
import com.demo.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AccountService accountService;
    private final Environment env;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

/*        String username = request.getParameter("username");
        String password = request.getParameter("password");*/
        try{
            RequestLogin creds =  new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class); //post방식

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getNickname(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );

        }catch (IOException e){
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication)
            throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        ResponseUser responseUser = accountService.getUser(user.getUsername());
        String token = Jwts.builder()
                .setSubject(responseUser.getNickname())
                .setExpiration(new Date(System.currentTimeMillis()+10*60*1000))
                .signWith(SignatureAlgorithm.HS512,env.getProperty("token.secret"))
                .compact();

        response.addHeader("token",token);
        response.addHeader("nickname",responseUser.getNickname());

    }
}
