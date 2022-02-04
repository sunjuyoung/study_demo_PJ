package com.project.study.service;

import com.project.study.domain.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {

    private static final String SECRET_KEY = "WeroiEF34rffsdf";

    public String create(Account account){
        Date expirDate = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS)
        );
        return Jwts.builder()
                //header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.ES512,SECRET_KEY)
                //payload에 들어갈 내용
                .setSubject(account.getNickname())
                .setIssuer("study app")//iss
                .setIssuedAt(new Date())
                .setExpiration(expirDate)//exp
                .compact();
    }

    public String validateAndGetUserId(String token){
        //parseClaimJws 메서드가 Base64로 디코딩 및 파싱
        //헤더와 페이로드를 setSignkey 로 넘어온 시크릿을 이용해 서명한 후 token의 서명과 비교
        //위조되지 않았다면 페이로드 리턴 위조라면 예외를 날림
        //그중 우리는 userId가 필요하므로 getBody를 부른다
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
