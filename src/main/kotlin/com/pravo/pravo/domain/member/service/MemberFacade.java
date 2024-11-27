package com.pravo.pravo.domain.member.service;

import com.pravo.pravo.domain.fine.model.FineLog;
import com.pravo.pravo.domain.fine.repository.FineLogRepository;
import com.pravo.pravo.domain.member.dto.MemberPaymentLogResponseDTO;
import com.pravo.pravo.domain.member.dto.MemberPointLogResponseDTO;
import com.pravo.pravo.domain.payment.enums.PaymentStatus;
import com.pravo.pravo.domain.payment.model.PaymentLog;
import com.pravo.pravo.domain.payment.repository.PaymentLogRepository;
import com.pravo.pravo.domain.point.model.PointLog;
import com.pravo.pravo.domain.point.repository.PointLogRepository;
import com.pravo.pravo.domain.promise.model.Promise;
import com.pravo.pravo.domain.promise.repository.PromiseRepository;
import com.pravo.pravo.global.error.ErrorCode;
import com.pravo.pravo.global.error.exception.NotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MemberFacade {

    private final PromiseRepository promiseRepository;
    private final PaymentLogRepository paymentLogRepository;
    private final PointLogRepository pointLogRepository;
    private final FineLogRepository fineLogRepository;

    public MemberFacade(PromiseRepository promiseRepository,
        PaymentLogRepository paymentLogRepository, PointLogRepository pointLogRepository,
        FineLogRepository fineLogRepository) {
        this.promiseRepository = promiseRepository;
        this.paymentLogRepository = paymentLogRepository;
        this.pointLogRepository = pointLogRepository;
        this.fineLogRepository = fineLogRepository;
    }

    public List<MemberPaymentLogResponseDTO> getMemberPaymentLog(Long memberId) {
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
