package com.pravo.pravo.domain.payment.repository;

import com.pravo.pravo.domain.payment.enums.PaymentStatus;
import com.pravo.pravo.domain.payment.model.PaymentLog;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, String> {

    Optional<PaymentLog> findByMemberIdAndPromiseId(Long memberId, Long promiseId);

    List<PaymentLog> findByMemberIdAndPaymentStatus(Long memberId, PaymentStatus paymentStatus);
}
