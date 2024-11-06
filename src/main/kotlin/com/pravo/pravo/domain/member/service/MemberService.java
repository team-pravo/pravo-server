package com.pravo.pravo.domain.member.service;

import com.pravo.pravo.domain.member.dto.LoginDTO;
import com.pravo.pravo.domain.member.dto.LoginResponseDTO;
import com.pravo.pravo.domain.member.model.Member;
import com.pravo.pravo.domain.member.repository.MemberRepository;
import com.pravo.pravo.global.jwt.JwtTokenProvider;
import com.pravo.pravo.global.jwt.JwtTokens;
import com.pravo.pravo.global.jwt.JwtTokensGenerator;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.pravo.pravo.global.common.error.exception.UnauthorizedException;
import com.pravo.pravo.global.common.error.ErrorCode;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokensGenerator jwtTokensGenerator;
    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String, String> redisTemplate;

    public MemberService(MemberRepository memberRepository, JwtTokensGenerator jwtTokensGenerator,
        RedisTemplate<String, String> redisTemplate, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokensGenerator = jwtTokensGenerator;
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponseDTO login(LoginDTO loginDTO) {
        // TODO: 이미 가입한 회원인지 확인 필요
        RandomNameGenerator nameGenerator = new RandomNameGenerator(memberRepository);
        String uniqueRandomName = nameGenerator.generateUniqueRandomName();

        Member socialLoginMember = new Member();
        socialLoginMember.setName(uniqueRandomName);
        socialLoginMember.setSocialId(loginDTO.getSocialId());
        Member loginMember = memberRepository.findBySocialId(socialLoginMember.getSocialId())
            .orElseGet(() -> memberRepository.save(socialLoginMember));

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        JwtTokens accessToken = jwtTokensGenerator.generate(loginMember.getId());
        loginResponseDTO.setJwtTokens(accessToken);
        loginResponseDTO.setMemberId(loginMember.getId());
        loginResponseDTO.setName(loginMember.getName());
        loginResponseDTO.setProfileImage(loginMember.getProfileImage());

        return loginResponseDTO;
    }

    public String logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        Long expiration = jwtTokenProvider.getExpiration(token);

        //**로그아웃 구분하기 위해 redis에 저장**
        redisTemplate.opsForValue()
            .set(request.getHeader("Authorization"),
                "logout token", expiration, TimeUnit.MILLISECONDS);

        return "Successfully logged out";
    }

    public void validateMemberById(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }
    }
}
