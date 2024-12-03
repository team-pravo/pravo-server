package com.pravo.pravo.domain.member.service;

import com.pravo.pravo.domain.fine.model.FineLog;
import com.pravo.pravo.domain.fine.repository.FineLogRepository;
import com.pravo.pravo.domain.member.dto.LoginRequestDTO;
import com.pravo.pravo.domain.member.dto.LoginResponseDTO;
import com.pravo.pravo.domain.member.dto.MemberPaymentLogResponseDTO;
import com.pravo.pravo.domain.member.dto.MemberPointLogResponseDTO;
import com.pravo.pravo.domain.member.dto.MyPageResponseDTO;
import com.pravo.pravo.domain.member.model.Member;
import com.pravo.pravo.domain.member.repository.MemberRepository;
import com.pravo.pravo.domain.payment.enums.PaymentStatus;
import com.pravo.pravo.domain.payment.model.PaymentLog;
import com.pravo.pravo.domain.payment.repository.PaymentLogRepository;
import com.pravo.pravo.domain.point.model.PointLog;
import com.pravo.pravo.domain.point.repository.PointLogRepository;
import com.pravo.pravo.domain.promise.model.Promise;
import com.pravo.pravo.domain.promise.repository.PromiseRepository;
import com.pravo.pravo.global.error.ErrorCode;
import com.pravo.pravo.global.error.exception.BaseException;
import com.pravo.pravo.global.error.exception.NotFoundException;
import com.pravo.pravo.global.error.exception.UnauthorizedException;
import com.pravo.pravo.global.external.s3.S3Service;
import com.pravo.pravo.global.jwt.JwtTokenProvider;
import com.pravo.pravo.global.jwt.JwtTokens;
import com.pravo.pravo.global.jwt.JwtTokensGenerator;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
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

    private final PromiseRepository promiseRepository;
    private final PaymentLogRepository paymentLogRepository;
    private final PointLogRepository pointLogRepository;
    private final FineLogRepository fineLogRepository;

    public MemberService(MemberRepository memberRepository, JwtTokensGenerator jwtTokensGenerator,
        RedisTemplate<String, String> redisTemplate, JwtTokenProvider jwtTokenProvider,
        S3Service s3Service, PromiseRepository promiseRepository,
        PaymentLogRepository paymentLogRepository, PointLogRepository pointLogRepository,
        FineLogRepository fineLogRepository) {
        this.memberRepository = memberRepository;
        this.jwtTokensGenerator = jwtTokensGenerator;
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
        this.s3Service = s3Service;
        this.promiseRepository = promiseRepository;
        this.paymentLogRepository = paymentLogRepository;
        this.pointLogRepository = pointLogRepository;
        this.fineLogRepository = fineLogRepository;
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

    public String logout(String token) {
        // remove Bearer
        token = token.substring(7);
        Long expiration = jwtTokenProvider.getExpiration(token) - System.currentTimeMillis();

        //**로그아웃 구분하기 위해 redis에 저장**
        redisTemplate.opsForValue().set(token, "logout token", expiration, TimeUnit.MILLISECONDS);

        return "Successfully logged out";
    }

    public void validateMemberById(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }
    }

    public MyPageResponseDTO fetchMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "멤버를 찾을 수 없습니다"));
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

    public List<MemberPaymentLogResponseDTO> getMemberPaymentLog(Long memberId) {
        List<PaymentLog> paymentLogs = paymentLogRepository.findByMemberIdAndPaymentStatus(
            memberId,
            List.of(PaymentStatus.COMPLETED, PaymentStatus.CANCELED));

        return paymentLogs.stream()
            .map(paymentLog -> new MemberPaymentLogResponseDTO(
                paymentLog.getPromise().getName(),
                paymentLog.getApprovedAt().toString(),
                paymentLog.getBalanceAmount(),
                paymentLog.getPaymentStatus().name()
            ))
            .collect(Collectors.toList());
    }

    public List<MemberPointLogResponseDTO> getMemberPointLog(Long memberId) {
        List<PointLog> pointLogs = pointLogRepository.findByMemberId(memberId);
        List<FineLog> fineLogs = fineLogRepository.findByMemberId(memberId);

        List<MemberPointLogResponseDTO> combinedLogs = new ArrayList<>();

        // pointlog
        combinedLogs.addAll(
            pointLogs.stream()
                .map(pointLog -> {
                    Promise promise = promiseRepository.findById(pointLog.getPromiseId())
                        .orElseThrow(
                            () -> new NotFoundException(ErrorCode.NOT_FOUND, "약속을 찾을 수 없습니다"));
                    return new MemberPointLogResponseDTO(
                        promise.getName(),
                        pointLog.getPointLogStatus().toString(),
                        pointLog.getAmount(),
                        pointLog.getUpdatedAt()
                    );
                })
                .toList()
        );

        // finelog
        combinedLogs.addAll(
            fineLogs.stream()
                .map(fineLog -> {
                    Promise promise = promiseRepository.findById(fineLog.getPromiseId())
                        .orElseThrow(
                            () -> new NotFoundException(ErrorCode.NOT_FOUND, "약속을 찾을 수 없습니다"));
                    return new MemberPointLogResponseDTO(
                        promise.getName(),
                        "FINE",
                        fineLog.getAmount(),
                        fineLog.getUpdatedAt()
                    );
                })
                .toList()
        );

        combinedLogs.sort(Comparator.comparing(MemberPointLogResponseDTO::pointDate).reversed());
        return combinedLogs;
    }
}
