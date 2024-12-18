package com.pravo.pravo.domain.payment.repository;

import com.pravo.pravo.domain.payment.enums.PaymentStatus;
import com.pravo.pravo.domain.payment.model.PaymentLog;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, String> {

    Optional<PaymentLog> findByMemberIdAndPromiseId(Long memberId, Long promiseId);

    @Query("""
            SELECT pl FROM PaymentLog pl
            JOIN FETCH pl.promise p
            WHERE pl.memberId = :memberId
            AND pl.paymentStatus IN :paymentStatuses
            AND pl.status = "DONE"
        """)
    List<PaymentLog> findByMemberIdAndPaymentStatus(Long memberId,
        List<PaymentStatus> paymentStatuses);
}
