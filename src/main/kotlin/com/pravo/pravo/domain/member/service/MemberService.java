package com.pravo.pravo.domain.member.service;

import com.pravo.pravo.domain.member.dto.LoginRequestDTO;
import com.pravo.pravo.domain.member.dto.LoginResponseDTO;
import com.pravo.pravo.domain.member.dto.MyPageResponseDTO;
import com.pravo.pravo.domain.member.model.Member;
import com.pravo.pravo.domain.member.repository.MemberRepository;
import com.pravo.pravo.global.error.ErrorCode;
import com.pravo.pravo.global.error.exception.BaseException;
import com.pravo.pravo.global.error.exception.NotFoundException;
import com.pravo.pravo.global.error.exception.UnauthorizedException;
import com.pravo.pravo.global.external.s3.S3Service;
import com.pravo.pravo.global.jwt.JwtTokenProvider;
import com.pravo.pravo.global.jwt.JwtTokens;
import com.pravo.pravo.global.jwt.JwtTokensGenerator;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokensGenerator jwtTokensGenerator;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final S3Service s3Service;

    public MemberService(MemberRepository memberRepository, JwtTokensGenerator jwtTokensGenerator,
        RedisTemplate<String, String> redisTemplate, JwtTokenProvider jwtTokenProvider,
        S3Service s3Service) {
        this.memberRepository = memberRepository;
        this.jwtTokensGenerator = jwtTokensGenerator;
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
        this.s3Service = s3Service;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDto) {
        // TODO: 이미 가입한 회원인지 확인 필요
        RandomNameGenerator nameGenerator = new RandomNameGenerator(memberRepository);
        String uniqueRandomName = nameGenerator.generateUniqueRandomName();

        Member socialLoginMember = new Member(uniqueRandomName, loginRequestDto.getSocialId());
        Member loginMember = memberRepository.findBySocialId(socialLoginMember.getSocialId())
            .orElseGet(() -> memberRepository.save(socialLoginMember));

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        JwtTokens accessToken = jwtTokensGenerator.generate(loginMember.getId());
        loginResponseDTO.setJwtTokens(accessToken);
        loginResponseDTO.setMemberId(loginMember.getId());
        loginResponseDTO.setName(loginMember.getName());
        loginResponseDTO.setProfileImageUrl(loginMember.getProfileImageUrl());

        return loginResponseDTO;
    }

    public String logout(String token) {
        // remove Bearer
        token = token.substring(7);
        Long expiration = jwtTokenProvider.getExpiration(token) - System.currentTimeMillis();

        //**로그아웃 구분하기 위해 redis에 저장**
        redisTemplate.opsForValue()
            .set(token, "logout token", expiration, TimeUnit.MILLISECONDS);

        return "Successfully logged out";
    }

    public void validateMemberById(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }
    }

    public MyPageResponseDTO fetchMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(
                () -> new NotFoundException(ErrorCode.NOT_FOUND, "멤버를 찾을 수 없습니다"));
        return MyPageResponseDTO.of(member);
    }

    public MyPageResponseDTO updateNameAndProfileImageUrl(Long memberId,
        String name,
        MultipartFile file) {
        Member updateMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "멤버를 찾을 수 없습니다"));
        String updateMemberName = updateMember.getName();
        if (!updateMemberName.equals(name)) {
            if (memberRepository.existsByName(name)) {
                throw new BaseException(ErrorCode.NAME_EXIST_ERROR);
            }
            updateMember.changeName(name);
        }
        if (file != null) {
            updateMember.changeProfileImageUrl(s3Service.uploadFile(file, "profile-image"));
        }
        memberRepository.save(updateMember);
        return MyPageResponseDTO.of(updateMember);
    }
}
