package com.pravo.pravo.domain.member.service;

import com.pravo.pravo.domain.member.dto.LoginDTO;
import com.pravo.pravo.domain.member.dto.LoginResponseDTO;
import com.pravo.pravo.domain.member.model.Member;
import com.pravo.pravo.domain.member.repository.MemberRepository;
import com.pravo.pravo.global.jwt.JwtTokensGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokensGenerator jwtTokensGenerator;

    public MemberService(MemberRepository memberRepository, JwtTokensGenerator jwtTokensGenerator) {
        this.memberRepository = memberRepository;
        this.jwtTokensGenerator = jwtTokensGenerator;
    }

    public LoginResponseDTO login(LoginDTO loginDTO) {
        RandomNameGenerator nameGenerator = new RandomNameGenerator();
        String uniqueRandomName = nameGenerator.generateUniqueRandomName();

        Member socialLoginMember = new Member();
        socialLoginMember.setName(uniqueRandomName);
        socialLoginMember.setSocialId(loginDTO.getSocialId());
        Member loginMember = memberRepository.findBySocialId(socialLoginMember.getSocialId())
            .orElseGet(() -> memberRepository.save(socialLoginMember));

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setJwtTokens(jwtTokensGenerator.generate(loginMember.getId()));
        loginResponseDTO.setMemberId(loginMember.getId());
        loginResponseDTO.setName(loginMember.getName());
        loginResponseDTO.setProfileImage(loginMember.getProfileImage());
        return loginResponseDTO;
    }
}
