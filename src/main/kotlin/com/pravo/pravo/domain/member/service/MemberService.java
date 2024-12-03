package com.pravo.pravo.domain.member.service;

import com.pravo.pravo.domain.member.dto.LoginRequestDTO;
import com.pravo.pravo.domain.member.dto.LoginResponseDTO;
import com.pravo.pravo.domain.member.dto.MyPageResponseDTO;
import com.pravo.pravo.domain.member.model.Member;
import com.pravo.pravo.domain.member.repository.MemberRepository;
import com.pravo.pravo.domain.promise.model.PromiseRole;
import com.pravo.pravo.domain.promise.repository.PromiseRoleRepository;
import com.pravo.pravo.global.error.ErrorCode;
import com.pravo.pravo.global.error.exception.BaseException;
import com.pravo.pravo.global.error.exception.NotFoundException;
import com.pravo.pravo.global.error.exception.UnauthorizedException;
import com.pravo.pravo.global.external.s3.S3Service;
import com.pravo.pravo.global.jwt.JwtTokenProvider;
import com.pravo.pravo.global.jwt.JwtTokens;
import com.pravo.pravo.global.jwt.JwtTokensGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MemberService {

    @Value("${spring.member.default-profile-image-url}")
    private String defaultProfileImageUrl;
    private final MemberRepository memberRepository;
    private final JwtTokensGenerator jwtTokensGenerator;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final S3Service s3Service;

    private final PromiseRoleRepository promiseRoleRepository;

    public MemberService(MemberRepository memberRepository, JwtTokensGenerator jwtTokensGenerator,
        RedisTemplate<String, String> redisTemplate, JwtTokenProvider jwtTokenProvider,
        S3Service s3Service, PromiseRoleRepository promiseRoleRepository) {
        this.memberRepository = memberRepository;
        this.jwtTokensGenerator = jwtTokensGenerator;
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
        this.s3Service = s3Service;
        this.promiseRoleRepository = promiseRoleRepository;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDto) {
        // TODO: 이미 가입한 회원인지 확인 필요
        RandomNameGenerator nameGenerator = new RandomNameGenerator(memberRepository);
        String uniqueRandomName = nameGenerator.generateUniqueRandomName();

        Member socialLoginMember = new Member(uniqueRandomName, loginRequestDto.getSocialId());
        Member loginMember = memberRepository.findBySocialId(socialLoginMember.getSocialId())
            .orElseGet(() -> {
                socialLoginMember.setProfileImageUrl(defaultProfileImageUrl);
                return memberRepository.save(socialLoginMember);
            });

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        JwtTokens accessToken = jwtTokensGenerator.generate(loginMember.getId());
        loginResponseDTO.setJwtTokens(accessToken);
        loginResponseDTO.setMemberId(loginMember.getId());
        loginResponseDTO.setName(loginMember.getName());
        loginResponseDTO.setProfileImageUrl(loginMember.getProfileImageUrl());

        return loginResponseDTO;
    }

    public void logout(String token) {
        // remove Bearer
        token = token.substring(7);
        Long expiration = jwtTokenProvider.getExpiration(token) - System.currentTimeMillis();

        //**로그아웃 구분하기 위해 redis에 저장**
        redisTemplate.opsForValue()
            .set(token, "logout token", expiration, TimeUnit.MILLISECONDS);
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

    public MyPageResponseDTO updateNameAndProfileImageUrl(Long memberId, String name,
        MultipartFile file, Boolean resetToDefaultImage) {
        Member updateMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "멤버를 찾을 수 없습니다"));

        // Update name if necessary
        updateMemberNameIfNeeded(updateMember, name);

        // Update profile image if necessary
        String oldProfileImageUrl = updateMember.getProfileImageUrl();
        updateProfileImage(updateMember, file, resetToDefaultImage);

        // Save changes
        memberRepository.save(updateMember);

        // Delete old image if needed
        deleteOldProfileImageIfNeeded(oldProfileImageUrl, updateMember.getProfileImageUrl());

        return MyPageResponseDTO.of(updateMember);
    }

    private void updateMemberNameIfNeeded(Member member, String name) {
        if (!member.getName().equals(name)) {
            if (memberRepository.existsByName(name)) {
                throw new BaseException(ErrorCode.NAME_EXIST_ERROR);
            }
            member.setName(name);
        }
    }

    private void updateProfileImage(Member member, MultipartFile file,
        Boolean resetToDefaultImage) {
        if (file != null) {
            member.setProfileImageUrl(s3Service.uploadFile(file, "profile-image"));
        } else if (resetToDefaultImage) {
            member.setProfileImageUrl(defaultProfileImageUrl);
        }
    }

    private void deleteOldProfileImageIfNeeded(String oldProfileImageUrl,
        String newProfileImageUrl) {
        if (oldProfileImageUrl != null && !oldProfileImageUrl.equals(newProfileImageUrl)) {
            s3Service.deleteFile(oldProfileImageUrl);
        }
    }

    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다"));
    }

    @Transactional
    public void withdrawMember(Long memberId, String token) {
        Member withdrawMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "멤버를 찾을 수 없습니다"));
        // TODO: 남은 포인트 처리
        List<PromiseRole> promiseRoles = promiseRoleRepository.findByMemberId(memberId);
        for (PromiseRole promiseRole : promiseRoles) {
            promiseRole.delete();
        }
        withdrawMember.delete();
        logout(token);
    }
}
