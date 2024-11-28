package com.pravo.pravo.domain.payment.repository;

import com.pravo.pravo.domain.payment.model.PaymentLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, String> {

    @Query("SELECT pr FROM PromiseRole pr JOIN FETCH pr.member WHERE pr.promise.id = :promiseId AND pr.member.id = :memberId")
    Optional<PaymentLog> findByMemberIdAndPromiseId(Long memberId, Long promiseId);
}
