package com.pravo.pravo.domain.member.service;

import com.pravo.pravo.domain.fine.model.FineLog;
import com.pravo.pravo.domain.fine.repository.FineLogRepository;
import com.pravo.pravo.domain.member.dto.LoginRequestDTO;
import com.pravo.pravo.domain.member.dto.LoginResponseDTO;
import com.pravo.pravo.domain.member.dto.MemberFineLogResponseDTO;
import com.pravo.pravo.domain.member.dto.MemberPaymentLogResponseDTO;
import com.pravo.pravo.domain.member.dto.MemberPointLogResponseDTO;
import com.pravo.pravo.domain.member.dto.MyPageResponseDTO;
import com.pravo.pravo.domain.member.model.Member;
import com.pravo.pravo.domain.member.repository.MemberRepository;
import com.pravo.pravo.domain.payment.enums.PaymentStatus;
import com.pravo.pravo.domain.payment.model.PaymentLog;
import com.pravo.pravo.domain.payment.repository.PaymentLogRepository;
import com.pravo.pravo.domain.point.model.PointLog;
import com.pravo.pravo.domain.point.model.PointLogStatus;
import com.pravo.pravo.domain.point.repository.PointLogRepository;
import com.pravo.pravo.domain.point.service.PointLogService;
import com.pravo.pravo.domain.promise.model.PromiseRole;
import com.pravo.pravo.domain.promise.model.enums.ParticipantStatus;
import com.pravo.pravo.domain.promise.model.enums.PromiseStatus;
import com.pravo.pravo.domain.promise.repository.PromiseRepository;
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
    private final PromiseRoleRepository promiseRoleRepository;
    private final PaymentLogRepository paymentLogRepository;
    private final PointLogRepository pointLogRepository;
    private final FineLogRepository fineLogRepository;
    private final PointLogService pointLogService;

    public MemberService(MemberRepository memberRepository, JwtTokensGenerator jwtTokensGenerator,
        RedisTemplate<String, String> redisTemplate, JwtTokenProvider jwtTokenProvider,
        S3Service s3Service, PromiseRepository promiseRepository,
        PromiseRoleRepository promiseRoleRepository,
        PaymentLogRepository paymentLogRepository, PointLogRepository pointLogRepository,
        FineLogRepository fineLogRepository, PointLogService pointLogService) {
        this.memberRepository = memberRepository;
        this.jwtTokensGenerator = jwtTokensGenerator;
        this.redisTemplate = redisTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
        this.s3Service = s3Service;
        this.promiseRoleRepository = promiseRoleRepository;
        this.paymentLogRepository = paymentLogRepository;
        this.pointLogRepository = pointLogRepository;
        this.fineLogRepository = fineLogRepository;
        this.pointLogService = pointLogService;
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

    @Transactional
    public void withdrawMember(Long memberId, String token) {
        Member withdrawMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "멤버를 찾을 수 없습니다"));
        List<PromiseRole> promiseRoles = promiseRoleRepository.findByMemberId(memberId);
        for (PromiseRole promiseRole : promiseRoles) {
            if (promiseRole.getPromise().getStatus() == PromiseStatus.READY
                && promiseRole.getStatus() == ParticipantStatus.READY) {
                {
                    throw new BaseException(ErrorCode.PROMISE_NOT_CANCELLED,
                        "진행 중인 약속이 있습니다. 약속을 먼저 취소해주세요.");
                }
            }
            promiseRole.delete();
        }
        pointLogService.saveWithdrawPointLog(PointLogStatus.MINUS, withdrawMember.getPoint(),
            memberId);
        withdrawMember.setPoint(0L);
        withdrawMember.delete();
    }

    public List<MemberPaymentLogResponseDTO> getMemberPaymentLog(Long memberId) {
        List<PaymentLog> paymentLogs = paymentLogRepository.findByMemberIdAndPaymentStatus(
            memberId,
            List.of(PaymentStatus.COMPLETED, PaymentStatus.CANCELED));

        return paymentLogs.stream()
            .map(paymentLog -> new MemberPaymentLogResponseDTO(
                paymentLog.getPromise().getName(),
                paymentLog.getBalanceAmount(),
                paymentLog.getPaymentStatus().name(),
                paymentLog.getApprovedAt().toString(),
                paymentLog.getUpdatedAt().toString()
            ))
            .collect(Collectors.toList());
    }

    public List<MemberPointLogResponseDTO> getMemberPointLog(Long memberId) {
        List<PointLog> pointLogs = pointLogRepository.findByMemberId(memberId);

        return pointLogs.stream()
            .map(pointLog -> new MemberPointLogResponseDTO(
                pointLog.getPromise().getName(),
                pointLog.getPointLogStatus().toString(),
                pointLog.getAmount(),
                pointLog.getUpdatedAt()
            ))
            .sorted(Comparator.comparing(MemberPointLogResponseDTO::pointDate).reversed())
            .toList();
    }

    public List<MemberFineLogResponseDTO> getMemberFineLog(Long memberId) {
        List<FineLog> fineLogs = fineLogRepository.findByMemberId(memberId);

        return fineLogs.stream()
            .map(fineLog -> new MemberFineLogResponseDTO(
                fineLog.getPromise().getName(),
                fineLog.getAmount(),
                fineLog.getUpdatedAt()
            ))
            .sorted(Comparator.comparing(MemberFineLogResponseDTO::fineDate).reversed())
            .toList();
    }
}
