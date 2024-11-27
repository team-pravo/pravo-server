package com.pravo.pravo.domain.member.service;

import com.pravo.pravo.domain.member.dto.MemberPaymentLogResponseDTO;
import com.pravo.pravo.domain.member.model.Member;
import com.pravo.pravo.domain.member.repository.MemberRepository;
import com.pravo.pravo.domain.payment.enums.PaymentStatus;
import com.pravo.pravo.domain.payment.model.PaymentLog;
import com.pravo.pravo.domain.payment.repository.PaymentLogRepository;
import com.pravo.pravo.domain.promise.model.Promise;
import com.pravo.pravo.domain.promise.repository.PromiseRepository;
import com.pravo.pravo.global.error.ErrorCode;
import com.pravo.pravo.global.error.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MemberFacade {

    private final MemberRepository memberRepository;
    private final PromiseRepository promiseRepository;
    private final PaymentLogRepository paymentLogRepository;

    public MemberFacade(MemberRepository memberRepository, PromiseRepository promiseRepository,
        PaymentLogRepository paymentLogRepository) {
        this.memberRepository = memberRepository;
        this.promiseRepository = promiseRepository;
        this.paymentLogRepository = paymentLogRepository;
    }

    public List<MemberPaymentLogResponseDTO> getMemberPaymentLog(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "멤버를 찾을 수 없습니다"));

        List<PaymentLog> paymentLogs = paymentLogRepository.findByMemberIdAndPaymentStatus(memberId,
            PaymentStatus.COMPLETED);

        return paymentLogs.stream()
            .map(paymentLog -> {
                Promise promise = promiseRepository.findById(paymentLog.getPromiseId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "약속을 찾을 수 없습니다"));

                return new MemberPaymentLogResponseDTO(
                    promise.getName(),
                    paymentLog.getApprovedAt().toString(),
                    paymentLog.getBalanceAmount()
                );
            })
            .collect(Collectors.toList());
    }
}
