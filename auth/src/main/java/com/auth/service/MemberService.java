package com.auth.service;

import com.auth.dto.Role;
import com.auth.dto.SignUpDTO;
import com.auth.entity.Member;
import com.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public Member saveMember(SignUpDTO signUpDTO){

        Member userSave = signUpDTO.createUserSave(signUpDTO, passwordEncoder);
        validDuplicateMember(userSave);
        return memberRepository.save(userSave);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByNickname(username);
        if(member == null){
            throw new UsernameNotFoundException(username);
        }

        return User.builder()
                .username(member.getNickname())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }


    private void validDuplicateMember(Member member) {
        if(memberRepository.existsByEmail(member.getEmail())){
            throw new IllegalStateException("이미 가입한 회원입니다");
        }
    }
}
